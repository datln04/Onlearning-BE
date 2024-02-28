package fpt.fall23.onlearn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "system_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String version;

    private String projectName;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    private Integer studyingTime;

    private String defaultImage;

    private Integer defaultQuizTime;

    private Integer waitingQuizTime;

    private String description;

    private Double commissionFee;

    private Double teacherCommissionFee;

    private Integer refundedTime = 3;


}
