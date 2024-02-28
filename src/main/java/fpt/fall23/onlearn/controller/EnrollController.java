package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.common.Paginate;
import fpt.fall23.onlearn.dto.course.CourseResponse;
import fpt.fall23.onlearn.dto.enroll.EnrollRequest;
import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.enroll.RefundRequest;
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.EnrollStatus;
import fpt.fall23.onlearn.enums.ResponseCode;
import fpt.fall23.onlearn.enums.TransactionStatus;
import fpt.fall23.onlearn.enums.TransactionType;
import fpt.fall23.onlearn.exception.ApiException;
import fpt.fall23.onlearn.repository.EnrollRepository;
import fpt.fall23.onlearn.repository.SyllabusRepository;
import fpt.fall23.onlearn.service.*;
import fpt.fall23.onlearn.util.CurrencyConverter;
import fpt.fall23.onlearn.util.DateUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/enroll")
@RequiredArgsConstructor
public class EnrollController {

    @Autowired
    EnrollService enrollService;

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    @Autowired
    WalletService walletService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    SyllabusRepository syllabusRepository;

    @GetMapping("/student/getCourseEnrolls")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Paginate<CourseResponse>> getCourseEnroll(@RequestParam(required = false) EnrollStatus status,
                                                                    @ParameterObject Pageable pageable) {
        Account acc = authenticationService.getCurrentAuthenticatedAccount()
                .orElseThrow(() -> new ApiException(ResponseCode.UNAUTHORIZED));
        Student student = studentService.findStudentByAccountId(acc.getId());
        Page<CourseResponse> coursePage = courseService.getCourseEnrolls(student.getId(), status, pageable);
        return new ResponseEntity<>(new Paginate<>(coursePage), HttpStatus.OK);
    }

    @GetMapping("/byId")
    @JsonView(EnrollView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Enroll> findEnrollById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(enrollService.getEnrollById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    private ResponseEntity<Enroll> saveEnroll(@RequestBody Enroll enroll) {
        return new ResponseEntity<>(enrollService.saveEnroll(enroll), HttpStatus.OK);
    }


    @Autowired
    EnrollRepository enrollRepository;

    @PostMapping("/enroll")
    @JsonView(EnrollView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Object> saveEnroll(@RequestBody EnrollRequest enrollRequest) {
        Student student = studentService.findStudentById(enrollRequest.getStudentId());
        if (student == null) {
            return new ResponseEntity<>("Không tồn tại học sinh", HttpStatus.BAD_REQUEST);
        }
        Account account = authenticationService.findAccountById(student.getAccount().getId());
        if (enrollService.checkIsBannedAction(account.getId())) {
//            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Bạn đã bị hạn chế hành động do quá số lần hoàn tiền", null);
            return new ResponseEntity<>("Bạn đã bị hạn chế hành động do quá số lần hoàn tiền", HttpStatus.BAD_REQUEST);
        }


        Course course = courseService.getCourseById(enrollRequest.getCourseId());

        Enroll enroll =
//                enrollService.findEnrollByStudentAndCourse(student.getId(), course.getId(), EnrollStatus.PROCESSING);
                enrollRepository.findEnrollByStudentIdAndCourseIdAndStatusIn(student.getId(), course.getId());
        if (enroll != null) {
            return new ResponseEntity<>("Đã tồn tại đăng kí", HttpStatus.BAD_REQUEST);
        } else {
            enroll = new Enroll();
        }

        Wallet wallet = walletService.getWalletByAccountId(student.getAccount().getId());
        if (wallet == null) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>("Không tìm thấy ví", HttpStatus.BAD_REQUEST);
        } else {
            if (wallet.getAmount() >= course.getPrice()) {
                enroll = enrollService.saveEnroll(enroll);
                wallet.setAmount(wallet.getAmount() - course.getPrice());
                Transaction transaction = new Transaction();
                transaction.setAccountName(account.getProfile().getFirstName() + " " + account.getProfile().getLastName());
                transaction.setAmount(course.getPrice());
                transaction.setDateProcess(LocalDateTime.now());
                transaction.setTransactionType(TransactionType.ENROLLED);
                transaction.setTransactionStatus(TransactionStatus.COMPLETED);
                transaction.setDescription(String.format("Người dùng %s vừa đăng kí khoá học %s với giá %s",
                        account.getProfile().getEmail(), course.getName(), course.getPrice()));
                transaction.setParentId(null);
                wallet = walletService.saveWallet(wallet);
                transaction.setWallet(wallet);
                transaction.setStudent(student);
                transaction.setEnroll(enroll);
                transactionService.saveTransaction(transaction);
            } else {
                return new ResponseEntity<>("Không đủ tiền trong ví", HttpStatus.BAD_REQUEST);
            }

        }
        enroll.setStudent(student);
        enroll.setCourse(course);
        enroll.setAmount(course.getPrice());
        enroll.setRequestDate(LocalDateTime.now());
        enroll.setStatus(EnrollStatus.PROCESSING);
        SystemConfig systemConfig = systemConfigService.getLastSystemConfig();
        enroll.setCommission(systemConfig.getCommissionFee());
        enroll.setCommissionAmount(course.getPrice() * systemConfig.getCommissionFee() / 100);
        enroll.setFinishDate(course.getCreateDate().plusMonths(course.getLimitTime()));

        //save syllabus active
        Syllabus syllabus = syllabusRepository.getSyllabusByCourseAndStatus(course.getId(), "Active");
        if (syllabus != null) {
            enroll.setSyllabus(syllabus);
        }

        enroll = enrollService.saveEnroll(enroll);
        return new ResponseEntity<>(enroll, HttpStatus.OK);
    }

    @PostMapping("/refund")
    @JsonView(TransactionView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResponseDTO<Transaction>> refundEnroll(@RequestBody RefundRequest refundRequest) {
        return new ResponseEntity<>(transactionService.doRefund(refundRequest), HttpStatus.OK);
    }

    @GetMapping("/byCourseId")
    @JsonView(EnrollView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Enroll>> findEnrollByCourseId(@RequestParam(name = "course_id") Long courseId) {
        return new ResponseEntity<>(enrollService.findEnrollByCourse(courseId), HttpStatus.OK);
    }

    @GetMapping("/byStudentId")
    @JsonView(EnrollView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Enroll>> findEnrollByStudentId(@RequestParam(name = "student_id") Long studentId) {
        return new ResponseEntity<>(enrollService.findEnrollByStudent(studentId), HttpStatus.OK);
    }

    @GetMapping("/byStudentId-courseId")
    @JsonView(EnrollView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<Enroll> findEnrollByStudentIdAndCourse(@RequestParam(name = "student_id") Long studentId,
                                                                  @RequestParam(name = "course_id") Long courseId) {
        return new ResponseEntity<>(enrollService.findEnrollByStudentAndCourse(studentId, courseId), HttpStatus.OK);
    }

    @GetMapping("/enrolls")
    @JsonView(EnrollView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<List<Enroll>> findAllEnrolls() {
        return new ResponseEntity<>(enrollService.getAllEnrolls(), HttpStatus.OK);
    }


    @DeleteMapping("/remove")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    private ResponseEntity<String> removeEnroll(@RequestParam(name = "enroll_id") Long id) {
        if (enrollService.removeEnroll(id)) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<>("FAIL", HttpStatus.OK);
    }
}
