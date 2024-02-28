package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feedback_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({FeedbackView.class})
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "feed_content_id")
    @JsonView({FeedbackView.class})
    private FeedContent feedContent;
    
    @JsonView({FeedbackView.class})
    private int rate;
    
    @JsonView({FeedbackView.class})
    private String type;
    
    @JsonView({FeedbackView.class})
    @Column(columnDefinition = "TEXT")
    private String text;

}
