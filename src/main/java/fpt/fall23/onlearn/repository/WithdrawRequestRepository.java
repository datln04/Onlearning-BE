package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.WithdrawalRequest;
import fpt.fall23.onlearn.enums.WithdrawalRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRequestRepository extends JpaRepository<WithdrawalRequest, Long> {

    List<WithdrawalRequest> findAllByTeacherId(Long teacherId);

    WithdrawalRequest findWithdrawalRequestByTeacherIdAndAndWithdrawalRequestStatus(Long teacherId, String status);

}
