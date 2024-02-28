package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.report.ReportView;
import fpt.fall23.onlearn.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView({ReportView.class})
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private LocalDate createTime;

    private Boolean status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;
}
