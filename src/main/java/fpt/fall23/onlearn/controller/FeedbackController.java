package fpt.fall23.onlearn.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.feedback.FeedbackDetailRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.feedback.DoFeedbackRequest;
import fpt.fall23.onlearn.dto.feedback.FeedbackView;
import fpt.fall23.onlearn.entity.Enroll;
import fpt.fall23.onlearn.entity.FeedContent;
import fpt.fall23.onlearn.entity.Feedback;
import fpt.fall23.onlearn.entity.FeedbackDetail;
import fpt.fall23.onlearn.service.EnrollService;
import fpt.fall23.onlearn.service.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    EnrollService enrollService;

    @PostMapping(path = "/save-feed-content")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<FeedContent> saveFeedContent(@RequestBody FeedContent feedContent) {
        return new ResponseEntity<FeedContent>(feedbackService.saveFeedContent(feedContent), HttpStatus.OK);
    }

    @GetMapping(path = "/feed-contents")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<FeedContent>> getAllFeedContent() {
        return new ResponseEntity<List<FeedContent>>(feedbackService.getAllFeedContents(), HttpStatus.OK);
    }

    @GetMapping(path = "/feedbacks-by-student")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(FeedbackView.class)
    public ResponseEntity<List<Feedback>> getAllFeedByStudentId(@RequestParam(name = "studentId") Long studentId) {
        return new ResponseEntity<List<Feedback>>(feedbackService.findAllByStudentId(studentId), HttpStatus.OK);
    }

    @GetMapping(path = "/by-course-student")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(FeedbackView.class)
    public ResponseEntity<List<Feedback>> getAllFeedByStudentAndCourse(@RequestParam(name = "courseId") Long courseId) {
        return new ResponseEntity<List<Feedback>>(feedbackService.findAllByCourseStudent(courseId), HttpStatus.OK);
    }

    @PostMapping(path = "/do-feedback")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(FeedbackView.class)
    public ResponseDTO<Feedback> doFeedback(@RequestBody DoFeedbackRequest doFeedbackRequest) {
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(doFeedbackRequest, feedback);

        if (feedbackService.getFeedbackByEnrollId(doFeedbackRequest.getEnrollId()) != null) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Đã tồn tại feedback", null);
        }

        Enroll enroll = enrollService.getEnrollById(doFeedbackRequest.getEnrollId());
        if (enroll != null) {
            feedback.setEnroll(enroll);
        }

        feedback = feedbackService.saveFeedback(feedback);
        Set<FeedbackDetail> feedbackDetails = new LinkedHashSet<>();

        if (doFeedbackRequest.getFeedbackDetailRequests() != null
                && doFeedbackRequest.getFeedbackDetailRequests().size() > 0) {
            Feedback finalFeedback = feedback;
            for (FeedbackDetailRequest feedbackDetailRequest :
                    doFeedbackRequest.getFeedbackDetailRequests()) {
                FeedbackDetail feedbackDetail = new FeedbackDetail();
                feedbackDetail.setId(null);
                feedbackDetail.setText(feedbackDetailRequest.getText() == null ? "" : feedbackDetailRequest.getText());
                feedbackDetail.setType(feedbackDetailRequest.getType());
                feedbackDetail.setRate(feedbackDetailRequest.getRate());
                feedbackDetail.setFeedback(finalFeedback);
                if (feedbackDetailRequest.getFeedContentId() != null) {
                    FeedContent feedContent = feedbackService.getFeedContentById(feedbackDetailRequest.getFeedContentId());
                    if (feedContent != null) {
                        feedbackDetail.setFeedContent(feedContent);
                    }
                }
                feedbackDetail = feedbackService.saveFeedbackDetail(feedbackDetail);
                feedbackDetails.add(feedbackDetail);

            }
        }
        feedback.setFeedbackDetails(feedbackDetails);
        feedback.setCreateDate(LocalDate.now());
        feedback.setStatus(true);
//        return new ResponseEntity<Feedback>(feedbackService.saveFeedback(feedback), HttpStatus.OK);
        feedback = feedbackService.saveFeedback(feedback);
        feedback.setFeedbackDetails(feedbackDetails);
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", feedback);
    }

    @GetMapping(path = "/ByCourse")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(FeedbackView.class)
    public ResponseEntity<List<Feedback>> findAllByCourseId(@RequestParam(name = "course_id") Long courseId) {
        return new ResponseEntity<List<Feedback>>(feedbackService.findAllByCourseId(courseId), HttpStatus.OK);
    }

    @GetMapping(path = "/feedbacks")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(FeedbackView.class)
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return new ResponseEntity<List<Feedback>>(feedbackService.getAllFeedbacks(), HttpStatus.OK);
    }


    @GetMapping(path = "/rating-by-teacher")
    //@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<BigDecimal> getRatingByTeacher(@RequestParam(name = "teacher_id") Long teacherId) {
        return feedbackService.getRatingByTeacherId(teacherId);
    }

}
