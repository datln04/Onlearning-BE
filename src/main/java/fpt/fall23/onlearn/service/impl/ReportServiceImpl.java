package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.entity.Report;
import fpt.fall23.onlearn.repository.ReportRepository;
import fpt.fall23.onlearn.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public ResponseDTO<List<Report>> getReportByTeacher(Long teacherId) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", reportRepository.findAllByTeacherId(teacherId));
    }

    @Override
    public ResponseDTO<List<Report>> getReportByStudent(Long studentId) {
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", reportRepository.findAllByStudentId(studentId));
    }

    @Override
    public ResponseDTO<List<Report>> getAllReports() {
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", reportRepository.findAll());
    }
}
