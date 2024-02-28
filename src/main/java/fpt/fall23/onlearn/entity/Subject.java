package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.course.CourseView;
import fpt.fall23.onlearn.dto.subject.SubjectView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(SubjectView.class)
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CourseView.class,SubjectView.class})
    private Long id;
    
    @JsonView({CourseView.class,SubjectView.class})
    private String name;
    private String description;
    private LocalDate createDate;
    @JsonView({CourseView.class,SubjectView.class})
    private double minPrice;
    private Boolean status;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Staff.class)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subject")
    @JsonIgnore
    private Set<Course> courses;

}
