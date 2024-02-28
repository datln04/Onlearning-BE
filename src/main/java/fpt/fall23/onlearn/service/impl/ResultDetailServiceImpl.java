package fpt.fall23.onlearn.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fpt.fall23.onlearn.dto.resultdetail.ResultDetailResponse;
import fpt.fall23.onlearn.dto.resultdetail.UsedAnswerResponse;
import fpt.fall23.onlearn.entity.UsedAnswer;
import fpt.fall23.onlearn.service.UsedAnswerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.fall23.onlearn.entity.ResultDetail;
import fpt.fall23.onlearn.repository.ResultDetailRepository;
import fpt.fall23.onlearn.service.ResultDetailService;

@Service
public class ResultDetailServiceImpl implements ResultDetailService {

    @Autowired
    ResultDetailRepository repository;

    @Override
    public ResultDetail saveResultDetail(ResultDetail resultDetail) {
        return repository.save(resultDetail);
    }

    @Autowired
    UsedAnswerService usedAnswerService;

    @Override
    public List<ResultDetailResponse> findResultDetailResponseByResultQuizId(Long resultQuizId) {
        List<ResultDetail> resultDetail = repository.findResultDetailsByResultQuizId(resultQuizId);
        List<ResultDetailResponse> resultDetailResponses = new ArrayList<>();
        for (ResultDetail detail : resultDetail) {
            ResultDetailResponse resultDetailResponse = new ResultDetailResponse();
            BeanUtils.copyProperties(detail, resultDetailResponse);
            List<UsedAnswer> usedAnswers = usedAnswerService.findAllByUsedQuestionId(detail.getUsedQuestion().getId());
            List<UsedAnswerResponse> usedAnswerResponses = new ArrayList<>();
            if (usedAnswers != null && usedAnswers.size() > 0) {
                for (UsedAnswer answer : usedAnswers) {
                    UsedAnswerResponse usedAnswerResponse = new UsedAnswerResponse();
                    BeanUtils.copyProperties(answer, usedAnswerResponse);
                    if (detail.getUsedAnswer().getId().equals(answer.getId())) {
                        usedAnswerResponse.setIsChoose(true);
                    }else{
                        usedAnswerResponse.setIsChoose(false);
                    }
                    usedAnswerResponses.add(usedAnswerResponse);
                }
            }
            resultDetailResponse.setUsedAnswerResponses(usedAnswerResponses);
            resultDetailResponses.add(resultDetailResponse);
        }
        return resultDetailResponses;
    }

    @Override
    public List<ResultDetail> findResultDetailByResultQuizId(Long resultQuizId) {
        return repository.findResultDetailsByResultQuizId(resultQuizId);
    }

    @Override
    public ResultDetail findResultDetailById(Long id) {
        Optional<ResultDetail> resultDetail = repository.findById(id);
        if (resultDetail.isPresent()) {
            return resultDetail.get();
        }
        return null;
    }

    @Override
    public List<ResultDetail> findResultDetailByIds(List<Long> ids) {
        return repository.findResultDetailByIdIn(ids);
    }


    @Override
    public List<ResultDetail> findResultDetailByStudentId(Long studentId) {
        return null;
    }
}
