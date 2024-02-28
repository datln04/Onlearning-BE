package fpt.fall23.onlearn.service;

import com.amazonaws.Response;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawTransactionRequest;
import fpt.fall23.onlearn.entity.WithdrawalRequest;
import fpt.fall23.onlearn.repository.WithdrawRequestRepository;

import java.util.List;

public interface WithdrawRequestService {

    WithdrawalRequest getWithdrawalRequestById(Long id);


    WithdrawalRequest saveWithdrawRequest(WithdrawalRequest withdrawalRequest);

    WithdrawalRequest findWithdrawByTeacherIdAndStatus(Long teacherId, String status);

    List<WithdrawalRequest> findPendingWithdrawalRequestsByTeacherId(Long teacherId);


    List<WithdrawalRequest> getWithdrawByTeacher(Long teacherId);

    ResponseDTO<WithdrawalRequest> withdrawalTransaction(WithdrawTransactionRequest withdrawTransactionRequest);


}
