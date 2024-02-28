package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feed_content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView({FeedbackView.class})
public class FeedContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Double proportion;
}
