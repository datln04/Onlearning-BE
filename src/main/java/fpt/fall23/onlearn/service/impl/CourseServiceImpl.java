package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.course.CourseRequest;
import fpt.fall23.onlearn.dto.course.CourseResponse;
import fpt.fall23.onlearn.dto.course.RejectCourseRequest;
import fpt.fall23.onlearn.dto.course.request.CourseFilter;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.CourseStatus;
import fpt.fall23.onlearn.enums.EnrollStatus;
import fpt.fall23.onlearn.enums.TransactionStatus;
import fpt.fall23.onlearn.enums.TransactionType;
import fpt.fall23.onlearn.repository.CourseRepository;
import fpt.fall23.onlearn.repository.TeacherRepository;
import fpt.fall23.onlearn.repository.TransactionRepository;
import fpt.fall23.onlearn.service.*;
import fpt.fall23.onlearn.util.CurrencyConverter;
import jakarta.persistence.EntityManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {


    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Autowired
    TeacherService teacherService;

    @Autowired
    SubjectService subjectService;


    @Override
    public Course teacherSaveCourse(CourseRequest courseRequest) {
        Course course = new Course();
        BeanUtils.copyProperties(courseRequest, course);
        Teacher teacher = teacherService.getTeacherById(courseRequest.getTeacherId());

        if (teacher != null) {
            course.setTeacher(teacher);
        }
        Optional<Subject> subject = subjectService.getSubjectById(courseRequest.getSubjectId());
        if (subject.isPresent()) {
            course.setSubject(subject.get());
        }

        if (course.getPrice() <= subject.get().getMinPrice()) {
            return null;
        }

        course = courseRepository.save(course);
        return course;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Course removeCourse(Long id) {
        List<Enroll> enrolls = enrollService.findEnrollsByCourseAndProcessing(id);
        if (!enrolls.isEmpty()) {
            return null;
        }
        Course course = courseRepository.findById(id).get();
        course.setStatus(CourseStatus.DEACTIVE);
        course = courseRepository.save(course);
        return course;
    }

    @Override
    public List<Course> getCourseBySubject(Long subjectId) {
        return courseRepository.findAllBySubjectId(subjectId);
    }

    @Override
    public List<Course> getCourseByTeacherId(Long teacherId) {
        return courseRepository.findALlByTeacherId(teacherId);
    }

    @Override
    public Page<CourseResponse> getCourseEnrolls(Long studentId, EnrollStatus status, Pageable pageable) {

        return courseRepository.findCourseEnrolls(studentId, status, pageable);
    }

    @Override
    public List<Course> getCourseByFilter(CourseFilter courseFilter) {
        return courseRepository.findCourseByFilter("%" + courseFilter.getValue() + "%", courseFilter.getMinPrice(), courseFilter.getMaxPrice());
    }

    @Override
    public List<Course> getCourseUnEnrolled(Long studentId, String status, String value) {
        return courseRepository.findCourseUnEnrolls(studentId, status, "%" + value + "%");
    }

    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public ResponseDTO<Course> approveCourse(Long id) {

        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy khóa học", null);
        }
        Course course = courseOpt.get();
        if (course.getStatus().equals(CourseStatus.ACTIVE)) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Khóa học đã được đăng kí", null);
        }
        Teacher teacher = course.getTeacher();
        if(teacher == null){
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy giáo viên", null);
        }

        SystemConfig systemConfig = systemConfigService.getLastSystemConfig();
        Wallet wallet = walletService.getWalletByAccountId(teacher.getAccount().getId());

        if(wallet == null){
            wallet = new Wallet();
            wallet.setAmount(0.0);
            wallet.setAccount(teacher.getAccount());
            walletService.saveWallet(wallet);
        }

        Double teacherFee =
                systemConfig.getTeacherCommissionFee() == null ? 0.0 : systemConfig.getTeacherCommissionFee();
        Double teacherFeeAmount =
                course.getPrice() * teacherFee / 100;
        if(wallet.getAmount() < teacherFeeAmount){
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Số dư ví của giáo viên không đủ", null);
        }

        wallet.setAmount(wallet.getAmount() - teacherFeeAmount);
        wallet = walletService.saveWallet(wallet);
        Transaction transaction = new Transaction();
        transaction.setAccountName(String.format("%s", teacher.getAccount().getUsername()));
        transaction.setAmount(teacherFeeAmount);
        transaction.setDateProcess(LocalDateTime.now());
        transaction.setDescription(String.format("Giảng viên %s đã thanh toán phí xét duyệt %s, cho khóa học (%s)", teacher.getAccount().getProfile().getEmail(), teacherFeeAmount, course.getName()));
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.SERVICE_CHARGE);
        transaction.setEnroll(null);
        transaction.setStudent(null);
        transaction.setTeacher(teacher);
        transaction.setWallet(wallet);

        transaction = transactionRepository.save(transaction);

        course.setStatus(CourseStatus.ACTIVE);
        course = courseRepository.save(course);
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", course);
    }


    @Autowired
    EnrollService enrollService;

    @Autowired
    RejectCourseService rejectCourseService;

    @Override
    public ResponseDTO<Course> rejectCourse(RejectCourseRequest rejectCourseRequest) {
        Optional<Course> courseOpt = courseRepository.findById(rejectCourseRequest.getCourseId());
        if (courseOpt.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy môn học", null);
        }
        Course course = courseOpt.get();
        if (course.getStatus().equals(CourseStatus.REJECT)) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Môn học đã bị từ chối", null);
        }
//        List<Enroll> enrolls = enrollService.findEnrollByCourse(course.getId());
//        if (enrolls != null) {
//            if (enrolls.size() > 0) {
//                for (Enroll enroll :
//                        enrolls) {
//                    if (enroll.getStatus().equals(EnrollStatus.PROCESSING)) {
//                        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Is student processing", null);
//                    }
//                }
//            }
//        }
        course.setStatus(CourseStatus.REJECT);
        course = courseRepository.save(course);

        RejectCourse rejectCourse = new RejectCourse();
        rejectCourse.setCourse(course);
        rejectCourse.setReason(rejectCourseRequest.getReason());
        rejectCourse.setDateReject(LocalDate.now());
        rejectCourseService.saveRejectCourse(rejectCourse);

        return new ResponseDTO<>(HttpStatus.OK.value(), "success", course);
    }


    @Override
    public Integer countTotalEnrolledByCourse(Long courseId) {
        List<Enroll> enrolls = enrollService.findEnrollByCourse(courseId);
        return enrolls.size();
    }

    @Override
    public List<Course> findAllByStatus(CourseStatus courseStatus) {
        return courseRepository.findAllByStatus(courseStatus);
    }


    @Autowired
    WalletService walletService;

    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public ResponseDTO<Transaction> chargeFeeForTeacher(Long teacherId, Long courseId) {
        Course course = getCourseById(courseId);
        Teacher teacher = teacherService.getTeacherById(teacherId);
        SystemConfig systemConfig = systemConfigService.getLastSystemConfig();
        Wallet wallet = walletService.getWalletByAccountId(teacher.getAccount().getId());

        Double teacherFee =
                systemConfig.getTeacherCommissionFee() == null ? 0.0 : systemConfig.getTeacherCommissionFee();
        Double teacherFeeAmount =
                course.getPrice() * teacherFee / 100;

        if (wallet == null || wallet.getAmount() < teacherFeeAmount) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Tiền trong ví không đủ", null);
        }
        wallet.setAmount(wallet.getAmount() - teacherFeeAmount);
        wallet = walletService.saveWallet(wallet);
        Transaction transaction = new Transaction();
        transaction.setAccountName(String.format("%s", teacher.getAccount().getUsername()));
        transaction.setAmount(teacherFeeAmount);
        transaction.setDateProcess(LocalDateTime.now());
        transaction.setDescription(String.format("Giảng viên %s đã thanh toán phí xét duyệt %s, cho khóa học (%s)", teacher.getAccount().getProfile().getEmail(), teacherFeeAmount, course.getName()));
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTransactionType(TransactionType.SERVICE_CHARGE);
        transaction.setEnroll(null);
        transaction.setStudent(null);
        transaction.setTeacher(teacher);
        transaction.setWallet(wallet);

        transaction = transactionRepository.save(transaction);

        return new ResponseDTO<>(HttpStatus.OK.value(), "success", transaction);
    }
}
