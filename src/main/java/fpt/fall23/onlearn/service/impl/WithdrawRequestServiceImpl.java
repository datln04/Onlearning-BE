package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.withdrawal.WithdrawTransactionRequest;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.TransactionStatus;
import fpt.fall23.onlearn.enums.TransactionType;
import fpt.fall23.onlearn.enums.WithdrawalRequestStatus;
import fpt.fall23.onlearn.repository.TransactionRepository;
import fpt.fall23.onlearn.repository.WithdrawRequestRepository;
import fpt.fall23.onlearn.service.*;
import fpt.fall23.onlearn.util.NumberUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WithdrawRequestServiceImpl implements WithdrawRequestService {

    @Autowired
    WithdrawRequestRepository withdrawRequestRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Autowired
    TeacherService teacherService;

    @Autowired
    WalletService walletService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<WithdrawalRequest> findPendingWithdrawalRequestsByTeacherId(Long teacherId) {
        String query = "SELECT * FROM withdrawal_request WHERE teacher_id = ? AND withdrawal_request_status = 'PENDING' ORDER BY id DESC ";
        return entityManager.createNativeQuery(query, WithdrawalRequest.class)
                .setParameter(1, teacherId)
                .getResultList();
    }

    @Override
    public WithdrawalRequest getWithdrawalRequestById(Long id) {
        Optional<WithdrawalRequest> withdrawalRequest = withdrawRequestRepository.findById(id);
        if (withdrawalRequest.isPresent()) {
            return withdrawalRequest.get();
        }
        return null;
    }

    @Override
    public WithdrawalRequest saveWithdrawRequest(WithdrawalRequest withdrawalRequest) {
        return withdrawRequestRepository.save(withdrawalRequest);
    }

    @Override
    public List<WithdrawalRequest> getWithdrawByTeacher(Long teacherId) {
        return withdrawRequestRepository.findAllByTeacherId(teacherId);
    }


    @Override
    public WithdrawalRequest findWithdrawByTeacherIdAndStatus(Long teacherId, String status) {
        return withdrawRequestRepository.findWithdrawalRequestByTeacherIdAndAndWithdrawalRequestStatus(teacherId, status);
    }

    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    EnrollService enrollService;

    @Override
    @Transactional
    public ResponseDTO<WithdrawalRequest> withdrawalTransaction(WithdrawTransactionRequest withdrawTransactionRequest) {

        Teacher teacher = teacherService.getTeacherById(withdrawTransactionRequest.getTeacherId());
        List<Transaction> transactions = transactionRepository.findAllById(withdrawTransactionRequest.getTransactions());

        if (transactions == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy giao dịch", null);
        }
        Double sum = 0.0;

        Double sumServiceCharge = 0.0;

        for (Transaction trans :
                transactions) {
            sum += trans.getAmount();

            Enroll enroll = enrollService.getEnrollById(trans.getEnroll().getId());
            if(enroll != null){
                sumServiceCharge += enroll.getCommissionAmount();
            }

        }
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setRequestComments(String.format("Giảng viên %s đã rút %s từ các giao dịch", teacher.getAccount().getProfile().getEmail(), sum));
        withdrawalRequest.setRequestDate(LocalDateTime.now());
        withdrawalRequest.setWithdrawalAmount(sum);
        withdrawalRequest.setWithdrawalRequestStatus(WithdrawalRequestStatus.SUCCESS);
        withdrawalRequest.setTeacher(teacherService.getTeacherById(withdrawTransactionRequest.getTeacherId()));
        withdrawalRequest = saveWithdrawRequest(withdrawalRequest);
        for (Transaction trans :
                transactions) {
            trans.setWithdrawalRequests(withdrawalRequest);
            transactionRepository.save(trans);
        }
        withdrawalRequest.setTransactions(StreamSupport.stream(transactions.spliterator(), false).collect(Collectors.toSet()));

        Wallet wallet = walletService.getWalletByAccountId(teacher.getAccount().getId());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setAmount(withdrawalRequest.getWithdrawalAmount());
        }
        wallet.setAmount(wallet.getAmount() + withdrawalRequest.getWithdrawalAmount() - sumServiceCharge);
        walletService.saveWallet(wallet);

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setTeacher(teacher);
        transaction.setDescription(String.format("Giảng viên %s đã rút %s từ các giao dịch", teacher.getAccount().getProfile().getEmail(), withdrawalRequest.getWithdrawalAmount()));
        transaction.setWallet(wallet);
        transaction.setDateProcess(LocalDateTime.now());
        transaction.setAmount(withdrawalRequest.getWithdrawalAmount());
        transaction.setAccountName(teacher.getAccount().getProfile().getEmail());
        transactionRepository.save(transaction);

        //Service charge !
        SystemConfig systemConfig = systemConfigService.getLastSystemConfig();
//        Double serviceChargeAmount = systemConfig.getTeacherCommissionFee() * withdrawalRequest.getWithdrawalAmount() / 100;
        Transaction serviceChargeTrans = new Transaction();
        serviceChargeTrans.setTransactionType(TransactionType.SERVICE_CHARGE);
        serviceChargeTrans.setTransactionStatus(TransactionStatus.COMPLETED);
        serviceChargeTrans.setTeacher(teacher);
        serviceChargeTrans.setDescription(String.format("Giảng viên %s thanh toán phí cho giao dịch đã đăng kí là %s", teacher.getAccount().getProfile().getEmail(), systemConfig.getCommissionFee(), sumServiceCharge));
        serviceChargeTrans.setWallet(wallet);
        serviceChargeTrans.setDateProcess(LocalDateTime.now());
        serviceChargeTrans.setAmount(sumServiceCharge);
        serviceChargeTrans.setAccountName(teacher.getAccount().getProfile().getEmail());
        transactionRepository.save(serviceChargeTrans);

        return new ResponseDTO<>(HttpStatus.OK.value(), "success", withdrawalRequest);
    }
}
