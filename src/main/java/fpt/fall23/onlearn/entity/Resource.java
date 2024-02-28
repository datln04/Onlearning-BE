package fpt.fall23.onlearn.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.dto.lesson.LessonView;
import fpt.fall23.onlearn.dto.resource.ResourceView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resource")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView({ResourceView.class})
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({ResourceView.class,LessonView.class})
    private Long id;

    @Column(columnDefinition = "TEXT")
    @JsonView({ResourceView.class,LessonView.class})
    private String content;
    @JsonView({ResourceView.class,LessonView.class})
    private String resourceType;
    @JsonView({ResourceView.class,LessonView.class})
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_id")
    @JsonIgnore
    private Lesson lesson;

}
