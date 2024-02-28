package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Transaction;

import java.util.List;
import java.util.Map;

public interface DashboardService {
    Double totalEnrolledByMonth(int month);

    Double totalRevenueEnrolledByMonth(int month);

    Double totalHoahongEnrolledByMonth(int month);

    Integer totalStudentAccount();

    List<Transaction> list7Transactions();

    List<Map<String, Double>> getTotalEnrolledByYear(int year);

    List<Map<String, Double>> getTotalCommisionFeeByYear(int year);

    List<Map<String, Double>> getTotalRequestFeeByYear(int year);



}
