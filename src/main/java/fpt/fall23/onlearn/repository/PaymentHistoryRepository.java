package fpt.fall23.onlearn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.fall23.onlearn.entity.PaymentHistory;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long>{
    List<PaymentHistory> findByStudentIdOrderByIdDesc(Long studentId);
    List<PaymentHistory> findByTeacherIdOrderByIdDesc(Long teacherId);
    List<PaymentHistory> findByWalletIdOrderByIdDesc(Long walletId);

    PaymentHistory findByOrderId(String orderId);

}
