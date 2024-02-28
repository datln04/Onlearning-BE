package fpt.fall23.onlearn.repository;

import java.util.Date;
import java.util.List;

import fpt.fall23.onlearn.enums.TransactionStatus;
import fpt.fall23.onlearn.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import fpt.fall23.onlearn.entity.Transaction;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTransactionByStudentId(Long studentId);

    List<Transaction> findTransactionByTeacherIdAndDateProcessBetween(Long teacherId, Date dateFrom, Date dateTo);

    Transaction findTransactionByEnrollIdAndTransactionStatusAndTransactionType(Long enrollId, TransactionStatus transactionStatus, TransactionType transactionType);

    List<Transaction> findAllByTransactionType(TransactionType transactionType);


    @Query(value = "SELECT t.id, t.account_name, t.amount, t.date_process, t.transaction_status, t.description, t.transaction_type, t.parent_id, t.student_id, t.teacher_id, t.wallet_id, t.enroll_id, t.withdraw_request_id " +
            "FROM transaction t " +
            "LEFT JOIN enroll e ON t.enroll_id = e.id " +
            "LEFT JOIN course c ON e.course_id = c.id " +

            "WHERE c.teacher_id = :teacherId " +
            "AND t.parent_id IS NULL " +
            "AND t.withdraw_request_id IS NULL " +
            "AND t.transaction_type = 'ENROLLED' " +
            "AND t.transaction_status = 'COMPLETED' " +
            "AND t.date_process < CURRENT_DATE - INTERVAL '7 days'", nativeQuery = true)
    List<Transaction> findTransactionForWithdraw(Long teacherId);

}
