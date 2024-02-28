package fpt.fall23.onlearn.dto.report;

import fpt.fall23.onlearn.enums.ReportType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportRequest {
    private Long id;
    private String content;
    private Boolean status;
    private Long teacherId;
    private Long studentId;

//    @Enumerated(EnumType.STRING)
//    private ReportType reportType;
}
