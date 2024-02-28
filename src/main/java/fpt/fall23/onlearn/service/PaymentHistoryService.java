package fpt.fall23.onlearn.service;

import java.util.List;

import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryFilter;
import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryFilterStudent;
import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryFilterTeacher;
import fpt.fall23.onlearn.entity.PaymentHistory;

public interface PaymentHistoryService {
    
    PaymentHistory savePaymentHistory(PaymentHistory paymentHistory);

    PaymentHistory getByPaymentHistoryId(Long id);

    PaymentHistory getByPaymentOrderId(String orderId);
    List<PaymentHistory> findByWalletId(Long walletId);
    List<PaymentHistory> findByStudentIdAndDateRange(PaymentHistoryFilterStudent paymentHistoryFilterStudent);
    List<PaymentHistory> findByTeacherIdAndDateRange(PaymentHistoryFilterTeacher paymentHistoryFilterTeacher);

    List<PaymentHistory> findByStudentIdWithFilter(PaymentHistoryFilter paymentHistoryFilter);
    List<PaymentHistory> findAllPaymentHistories();


}
