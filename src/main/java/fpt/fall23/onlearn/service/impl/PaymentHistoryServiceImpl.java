package fpt.fall23.onlearn.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryFilter;
import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryFilterStudent;
import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryFilterTeacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.fall23.onlearn.entity.PaymentHistory;
import fpt.fall23.onlearn.repository.PaymentHistoryRepository;
import fpt.fall23.onlearn.service.PaymentHistoryService;

@Service
public class PaymentHistoryServiceImpl implements PaymentHistoryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    PaymentHistoryRepository paymentHistoryRepository;


    @Override
    public PaymentHistory savePaymentHistory(PaymentHistory paymentHistory) {
        return paymentHistoryRepository.save(paymentHistory);
    }

    @Override
    public PaymentHistory getByPaymentHistoryId(Long id) {
        Optional<PaymentHistory> paymentHistory = paymentHistoryRepository.findById(id);
        if (paymentHistory.isPresent()) {
            return paymentHistory.get();
        }
        return null;
    }

    @Override
    public List<PaymentHistory> findByWalletId(Long walletId) {
        return paymentHistoryRepository.findByWalletIdOrderByIdDesc(walletId);
    }

    @Override
    public List<PaymentHistory> findByStudentIdAndDateRange(PaymentHistoryFilterStudent paymentHistoryFilterStudent) {
        Long studentId = paymentHistoryFilterStudent.getStudentId();
        LocalDate startDate = paymentHistoryFilterStudent.getStartDate();
        LocalDate endDate = paymentHistoryFilterStudent.getEndDate();

        String nativeQuery = "SELECT * FROM payment_history WHERE student_id = :studentId";

        if (startDate != null && endDate != null) {
            LocalDateTime startOfDay = startDate.atStartOfDay();
            LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

            Date startDateConverted = java.sql.Timestamp.valueOf(startOfDay);
            Date endDateConverted = java.sql.Timestamp.valueOf(endOfDay);

            nativeQuery += " AND transaction_date BETWEEN :startDate AND :endDate";
            nativeQuery += " ORDER BY transaction_date desc";

            Query query = entityManager.createNativeQuery(nativeQuery, PaymentHistory.class);
            query.setParameter("studentId", studentId);
            query.setParameter("startDate", startDateConverted);
            query.setParameter("endDate", endDateConverted);

            return query.getResultList();
        } else {
            nativeQuery += " ORDER BY transaction_date desc";
            Query query = entityManager.createNativeQuery(nativeQuery, PaymentHistory.class);
            query.setParameter("studentId", studentId);

            return query.getResultList();
        }
    }


    @Override
    public List<PaymentHistory> findByStudentIdWithFilter(PaymentHistoryFilter filter) {
        StringBuilder nativeQuery = new StringBuilder("SELECT * FROM payment_history WHERE 1=1");

        if (filter.getValue() != null) {
            nativeQuery.append(" AND payment_method like :value");
        }

        if (filter.getStatus() != null) {
            nativeQuery.append(" AND payment_history_status like :status");
        }

        if (filter.getType() != null) {
            nativeQuery.append(" AND payment_history_type like :type");
        }

        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            nativeQuery.append(" AND transaction_date BETWEEN :startDate AND :endDate");
        }

        if (filter.getOrderBy() != null && filter.getOrderBy().trim().length() > 0) {
            nativeQuery.append(" ORDER BY ").append(filter.getOrderBy());
        }

        Query query = entityManager.createNativeQuery(nativeQuery.toString(), PaymentHistory.class);

        if (filter.getValue() != null) {
            query.setParameter("value", "%" + filter.getValue() + "%");
        }

        if (filter.getStatus() != null) {
            query.setParameter("status", "%" + filter.getStatus() + "%");
        }

        if (filter.getType() != null) {
            query.setParameter("type", "%" + filter.getType() + "%");
        }


        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            query.setParameter("startDate", filter.getStartDate());
            query.setParameter("endDate", filter.getEndDate());
        }

        return query.getResultList();
    }

    @Override
    public List<PaymentHistory> findByTeacherIdAndDateRange(PaymentHistoryFilterTeacher paymentHistoryFilterTeacher) {
        Long teacherId = paymentHistoryFilterTeacher.getTeacherId();
        LocalDate startDate = paymentHistoryFilterTeacher.getStartDate();
        LocalDate endDate = paymentHistoryFilterTeacher.getEndDate();

        String nativeQuery = "SELECT * FROM payment_history WHERE teacher_id = :teacherId";

        if (startDate != null && endDate != null) {
            LocalDateTime startOfDay = startDate.atStartOfDay();
            LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

            Date startDateConverted = java.sql.Timestamp.valueOf(startOfDay);
            Date endDateConverted = java.sql.Timestamp.valueOf(endOfDay);

            nativeQuery += " AND transaction_date BETWEEN :startDate AND :endDate";
            nativeQuery += " ORDER BY transaction_date desc";
            Query query = entityManager.createNativeQuery(nativeQuery, PaymentHistory.class);
            query.setParameter("teacherId", teacherId);
            query.setParameter("startDate", startDateConverted);
            query.setParameter("endDate", endDateConverted);

            return query.getResultList();
        } else {
            nativeQuery += " ORDER BY transaction_date desc";
            Query query = entityManager.createNativeQuery(nativeQuery, PaymentHistory.class);
            query.setParameter("teacherId", teacherId);

            return query.getResultList();
        }
    }

    @Override
    public List<PaymentHistory> findAllPaymentHistories() {
        return paymentHistoryRepository.findAll();
    }


    @Override
    public PaymentHistory getByPaymentOrderId(String orderId) {
        return paymentHistoryRepository.findByOrderId(orderId);
    }
}
