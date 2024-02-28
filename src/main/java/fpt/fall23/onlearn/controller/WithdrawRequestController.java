package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import fpt.fall23.onlearn.dto.withdrawal.AcceptWithdrawRequest;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawRequestView;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawTransactionRequest;
import fpt.fall23.onlearn.entity.Teacher;
import fpt.fall23.onlearn.entity.Transaction;
import fpt.fall23.onlearn.entity.Wallet;
import fpt.fall23.onlearn.entity.WithdrawalRequest;
import fpt.fall23.onlearn.enums.WithdrawalRequestStatus;
import fpt.fall23.onlearn.repository.TransactionRepository;
import fpt.fall23.onlearn.service.TeacherService;
import fpt.fall23.onlearn.service.TransactionService;
import fpt.fall23.onlearn.service.WalletService;
import fpt.fall23.onlearn.service.WithdrawRequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1/withdraw-request")
public class WithdrawRequestController {


    @Autowired
    WithdrawRequestService withdrawRequestService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    TeacherService teacherService;

//    @GetMapping("/transaction-teacher")
//    @JsonView({WithdrawRequestView.class})
//    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
//    public ResponseEntity<Object> getTransactionForWithdraw(@RequestParam(name = "teacher_id") Long teacherId) {
//
//        List<Transaction> transactions = transactionService.findTransactionForWithdraw(teacherId);
//        Double sum = 0.0;
//        for (Transaction trans :
//                transactions) {
//            sum += trans.getAmount();
//        }
//        if (transactions.size() == 0) {
//            return new ResponseEntity<>("Not have any transaction !", HttpStatus.BAD_REQUEST);
//        } else {
//            List<WithdrawalRequest> withdrawalRequests = withdrawRequestService.findPendingWithdrawalRequestsByTeacherId(teacherId);
//            WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
//            if (withdrawalRequests.size() == 0) {
//                withdrawalRequest.setRequestComments("teacher withdraw money from transaction");
//                withdrawalRequest.setRequestDate(new Date());
//                withdrawalRequest.setWithdrawalAmount(sum);
//                withdrawalRequest.setWithdrawalRequestStatus(WithdrawalRequestStatus.PENDING);
//                withdrawalRequest.setTeacher(teacherService.getTeacherById(teacherId));
//            } else {
//                return new ResponseEntity<>("Already request !", HttpStatus.BAD_REQUEST);
//            }
//            return new ResponseEntity<>(withdrawRequestService.saveWithdrawRequest(withdrawalRequest), HttpStatus.OK);
//        }
//    }



    @PostMapping("/withdraw-trasaction")
    @JsonView({WithdrawRequestView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<WithdrawalRequest> withdrawTransaction(@RequestBody WithdrawTransactionRequest withdrawTransactionRequest) {
        return withdrawRequestService.withdrawalTransaction(withdrawTransactionRequest);
    }


    @Autowired
    WalletService walletService;

//    @PostMapping("/accept-withdraw")
//    @JsonView({WithdrawRequestView.class})
//    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
//    public ResponseEntity<Object> acceptWithdrawRequest(@RequestBody AcceptWithdrawRequest acceptWithdrawRequest) {
//        Teacher teacher = teacherService.getTeacherById(acceptWithdrawRequest.getTeacherId());
//        if (teacher == null) {
//            return new ResponseEntity<>("Not found teacher", HttpStatus.BAD_REQUEST);
//        }
//
//        WithdrawalRequest withdrawalRequest = withdrawRequestService.getWithdrawalRequestById(acceptWithdrawRequest.getWithdrawId());
//        if (withdrawalRequest == null) {
//            return new ResponseEntity<>("Not found withdraw", HttpStatus.BAD_REQUEST);
//        }
//
//        List<Transaction> transactions = transactionService.findTransactionForWithdraw(teacher.getId());
//        for (Transaction transaction :
//                transactions) {
//            transaction.setWithdrawalRequests(withdrawalRequest);
//            transactionService.saveTransaction(transaction);
//        }
//
//        withdrawalRequest.setWithdrawalRequestStatus(WithdrawalRequestStatus.SUCCESS);
//
//        Wallet wallet = walletService.getWalletByAccountId(teacher.getAccount().getId());
//        if (wallet == null) {
//            wallet = new Wallet();
//            wallet.setAmount(withdrawalRequest.getWithdrawalAmount());
//        }
//        wallet.setAmount(wallet.getAmount() + withdrawalRequest.getWithdrawalAmount());
//        walletService.saveWallet(wallet);
//
//        withdrawalRequest = withdrawRequestService.saveWithdrawRequest(withdrawalRequest);
//        withdrawalRequest.setTransactions(StreamSupport.stream(transactions.spliterator(), false).collect(Collectors.toSet()));
//        return new ResponseEntity<>(withdrawalRequest, HttpStatus.OK);
//    }


    @GetMapping("/by-teacher")
    @JsonView({WithdrawRequestView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<WithdrawalRequest>> findAllByTeacherId(@RequestParam(name = "teacher_id") Long teacherId) {
        return new ResponseEntity<>(withdrawRequestService.getWithdrawByTeacher(teacherId), HttpStatus.OK);
    }

    @GetMapping("/by-id")
    @JsonView({WithdrawRequestView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<WithdrawalRequest> findAllById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(withdrawRequestService.getWithdrawalRequestById(id), HttpStatus.OK);
    }




}
