package fpt.fall23.onlearn.service;

import java.util.Date;
import java.util.List;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.enroll.RefundRequest;
import fpt.fall23.onlearn.dto.transaction.TransactionFilterStudent;
import fpt.fall23.onlearn.dto.transaction.TransactionFilterTeacher;
import fpt.fall23.onlearn.entity.Transaction;


public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    Transaction getTransactionById(Long id);
    List<Transaction> getTransactionsByStudentIdAndDateRange(TransactionFilterStudent transactionFilterStudent);
    List<Transaction> getTransactionsByTeacherIdAndDateRange(TransactionFilterTeacher transactionFilterTeacher);

    List<Transaction> findTransactionForWithdraw(Long teacherId);
    
    List<Transaction> findAllTransactions();

    ResponseDTO<Transaction> doRefund(RefundRequest refundRequest);

}
