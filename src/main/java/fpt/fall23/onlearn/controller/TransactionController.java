package fpt.fall23.onlearn.controller;

import java.util.Date;
import java.util.List;

import fpt.fall23.onlearn.dto.transaction.TransactionFilterStudent;
import fpt.fall23.onlearn.dto.transaction.TransactionFilterTeacher;
import org.springdoc.core.annotations.ParameterObject;
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
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import fpt.fall23.onlearn.entity.Transaction;
import fpt.fall23.onlearn.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(path = "/by-id")
    @JsonView({TransactionView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Transaction> getById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/save")
    @JsonView({TransactionView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionService.saveTransaction(transaction), HttpStatus.OK);
    }

    @GetMapping("/by-teacher")
    @JsonView({TransactionView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Transaction>> getByTeacherId(@ParameterObject TransactionFilterTeacher transactionFilterTeacher) {
        return new ResponseEntity<>(transactionService.getTransactionsByTeacherIdAndDateRange(transactionFilterTeacher), HttpStatus.OK);
    }

    @GetMapping("/by-student")
    @JsonView({TransactionView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Transaction>> getByStudentId(@ParameterObject TransactionFilterStudent transactionFilterStudent) {
        return new ResponseEntity<>(transactionService.getTransactionsByStudentIdAndDateRange(transactionFilterStudent), HttpStatus.OK);
    }

    @GetMapping("/transaction-for-withdraw")
    @JsonView({TransactionView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Transaction>> getTransactionForWithdraw(@RequestParam(name = "teacher_id") Long teacherId) {
        return new ResponseEntity<>(transactionService.findTransactionForWithdraw(teacherId), HttpStatus.OK);
    }


    @GetMapping("/transactions")
    @JsonView({TransactionView.class})
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Transaction>> getAllTransaction() {
        return new ResponseEntity<>(transactionService.findAllTransactions(), HttpStatus.OK);
    }


}
