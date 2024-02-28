package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByTeacherId(Long teacherId);
    List<Report> findAllByStudentId(Long studentId);
}
