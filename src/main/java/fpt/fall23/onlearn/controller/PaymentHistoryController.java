package fpt.fall23.onlearn.controller;

import java.util.List;

import fpt.fall23.onlearn.dto.paymenthistory.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.PaymentHistory;
import fpt.fall23.onlearn.entity.Student;
import fpt.fall23.onlearn.entity.Teacher;
import fpt.fall23.onlearn.entity.Wallet;
import fpt.fall23.onlearn.service.AuthenticationService;
import fpt.fall23.onlearn.service.PaymentHistoryService;
import fpt.fall23.onlearn.service.StudentService;
import fpt.fall23.onlearn.service.TeacherService;
import fpt.fall23.onlearn.service.WalletService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/payment_history")
public class PaymentHistoryController {
    // PaymentHistory savePaymentHistory(PaymentHistory paymentHistory);
    // PaymentHistory getByPaymentHistoryId(Long id);
    // List<PaymentHistory> findByWalletId(Long walletId);
    // List<PaymentHistory> findByStudentId(Long studentId);
    // List<PaymentHistory> findByTeacherId(Long teacherId);
    // List<PaymentHistory> findAllPaymentHistories();

    @Autowired
    PaymentHistoryService paymentHistoryService;

    @Autowired
    StudentService studentService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    WalletService walletService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/save")
    @JsonView(PaymentHistoryView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<PaymentHistory> savePaymentHistory(@RequestBody PaymentHistoryRequest paymentHistoryRequest) {
        PaymentHistory paymentHistory = new PaymentHistory();
        BeanUtils.copyProperties(paymentHistoryRequest, paymentHistory);

        Wallet wallet = walletService.getWalletById(paymentHistoryRequest.getWalletId());
        if (wallet != null) {
            paymentHistory.setWallet(wallet);
        }

        Student student = studentService.findStudentById(paymentHistoryRequest.getStudentId());
        if (student != null) {
            paymentHistory.setStudent(student);
        }

        Teacher teacher = teacherService.getTeacherById(paymentHistoryRequest.getTeacherId());
        if (teacher != null) {
            paymentHistory.setTeacher(teacher);
        }

        Account account = authenticationService.findAccountById(paymentHistoryRequest.getAccountId());
        if (account != null) {
            paymentHistory.setAccount(account);
        }

        return new ResponseEntity<>(paymentHistoryService.savePaymentHistory(paymentHistory), HttpStatus.OK);
    }

    @GetMapping("/by-id")
    @JsonView(PaymentHistoryView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<PaymentHistory> getPaymentHistoryById(@RequestParam(name = "history_id") long id) {
        return new ResponseEntity<>(paymentHistoryService.getByPaymentHistoryId(id), HttpStatus.OK);
    }

    @GetMapping("/by-wallet")
    @JsonView(PaymentHistoryView.class)
    public ResponseEntity<List<PaymentHistory>> getPaymentHistoryByWallet(
            @RequestParam(name = "wallet_id") long walletId) {
        return new ResponseEntity<>(paymentHistoryService.findByWalletId(walletId), HttpStatus.OK);
    }

    @GetMapping("/by-student")
    @JsonView(PaymentHistoryView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<PaymentHistory>> getPaymentHistoryByStudent(@ParameterObject PaymentHistoryFilterStudent paymentHistoryFilterStudent) {
        return new ResponseEntity<>(paymentHistoryService.findByStudentIdAndDateRange(paymentHistoryFilterStudent), HttpStatus.OK);
    }

    @GetMapping("/by-teacher")
    @JsonView(PaymentHistoryView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<PaymentHistory>> getPaymentHistoryByTeacher(
            @ParameterObject PaymentHistoryFilterTeacher paymentHistoryFilterTeacher) {
        return new ResponseEntity<>(paymentHistoryService.findByTeacherIdAndDateRange(paymentHistoryFilterTeacher), HttpStatus.OK);
    }

    @PostMapping("/by-student-filter")
    @JsonView(PaymentHistoryView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<PaymentHistory>> getPaymentHistoryByStudentWithFilter(
            @RequestBody PaymentHistoryFilter paymentHistoryFilter) {
        return new ResponseEntity<>(paymentHistoryService.findByStudentIdWithFilter(paymentHistoryFilter), HttpStatus.OK);
    }



    @GetMapping("/histories")
    @JsonView(PaymentHistoryView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<PaymentHistory>> getPaymentHistories() {
        return new ResponseEntity<>(paymentHistoryService.findAllPaymentHistories(), HttpStatus.OK);
    }

}
