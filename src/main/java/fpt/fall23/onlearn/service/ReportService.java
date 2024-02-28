package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.entity.Report;

import java.util.List;

public interface ReportService {
    Report save(Report report);

    ResponseDTO<List<Report>> getReportByTeacher(Long teacherId);
    ResponseDTO<List<Report>> getReportByStudent(Long studentId);

    ResponseDTO<List<Report>> getAllReports();



}
