package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Enroll;
import fpt.fall23.onlearn.entity.Transaction;
import fpt.fall23.onlearn.enums.TransactionType;
import fpt.fall23.onlearn.repository.EnrollRepository;
import fpt.fall23.onlearn.repository.StudentRepository;
import fpt.fall23.onlearn.repository.TransactionRepository;
import fpt.fall23.onlearn.service.DashboardService;
import fpt.fall23.onlearn.service.EnrollService;
import fpt.fall23.onlearn.service.StudentService;
import fpt.fall23.onlearn.service.TransactionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    EnrollRepository enrollRepository;
    @Autowired
    StudentService studentService;
    StudentRepository studentRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Double totalEnrolledByMonth(int month) {
        String query = "SELECT COALESCE(SUM(amount), 0) FROM enroll WHERE EXTRACT(MONTH FROM request_date) = ?";
        Object result = entityManager.createNativeQuery(query)
                .setParameter(1, month)
                .getSingleResult();
        if (result == null) {
            return 0.0;
        }
        return Double.valueOf(result.toString());
    }

    @Override
    public Double totalRevenueEnrolledByMonth(int month) {
        return null;
    }

    @Override
    public Double totalHoahongEnrolledByMonth(int month) {
        return null;
    }

    @Override
    public Integer totalStudentAccount() {
        return null;
    }

    @Override
    public List<Transaction> list7Transactions() {
        return null;
    }

    @Override
    public List<Map<String, Double>> getTotalEnrolledByYear(int year) {
        List<Map<String, Double>> monthlyValues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.getMonth().length(startOfMonth.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
            List<Enroll> enrollmentsInMonth = enrollRepository.getAllEnrollByStatusDone().stream()
                    .filter(enroll -> enroll.getRequestDate().isAfter(startOfMonth) && enroll.getRequestDate().isBefore(endOfMonth))
                    .collect(Collectors.toList());
            Double totalValue = enrollmentsInMonth.stream()
                    .mapToDouble(enroll -> enroll.getAmount() + enroll.getCommissionAmount())
                    .sum();
            Map<String, Double> monthValue = new HashMap<>();
            monthValue.put(String.format("%s", month), totalValue);
            monthlyValues.add(monthValue);
        }
        return monthlyValues;
    }

    @Override
    public List<Map<String, Double>> getTotalCommisionFeeByYear(int year) {
        List<Map<String, Double>> monthlyValues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.getMonth().length(startOfMonth.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
            List<Enroll> enrollmentsInMonth = enrollRepository.getAllEnrollByStatusDone().stream()
                    .filter(enroll -> enroll.getRequestDate().isAfter(startOfMonth) && enroll.getRequestDate().isBefore(endOfMonth))
                    .collect(Collectors.toList());
            Double totalValue = enrollmentsInMonth.stream()
                    .mapToDouble(enroll -> enroll.getCommissionAmount())
                    .sum();
            Map<String, Double> monthValue = new HashMap<>();
            monthValue.put(String.format("%s", month), totalValue);
            monthlyValues.add(monthValue);
        }
        return monthlyValues;
    }

    @Override
    public List<Map<String, Double>> getTotalRequestFeeByYear(int year) {
        List<Map<String, Double>> monthlyValues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.getMonth().length(startOfMonth.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
            List<Transaction> transactionsInMonth = transactionRepository.findAllByTransactionType(TransactionType.SERVICE_CHARGE).stream()
                    .filter(transaction -> transaction.getDateProcess().isAfter(startOfMonth) && transaction.getDateProcess().isBefore(endOfMonth))
                    .collect(Collectors.toList());
            Double totalValue = transactionsInMonth.stream()
                    .mapToDouble(transaction -> transaction.getAmount())
                    .sum();
            Map<String, Double> monthValue = new HashMap<>();
            monthValue.put(String.format("%s", month), totalValue);
            monthlyValues.add(monthValue);
        }
        return monthlyValues;
    }
}
