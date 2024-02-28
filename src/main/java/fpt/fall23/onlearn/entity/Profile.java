package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.enroll.EnrollView;
import fpt.fall23.onlearn.dto.report.ReportView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "profile")
@AllArgsConstructor
@NoArgsConstructor
@JsonView({CourseView.class, ReportView.class})
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avatar;

    private String phone;

    private String firstName;

    private String lastName;

    @JsonView({CourseView.class, EnrollView.class})
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    private LocalDate dateOfBirth;

    private Boolean status;

}
