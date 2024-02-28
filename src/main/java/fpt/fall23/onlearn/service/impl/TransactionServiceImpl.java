package fpt.fall23.onlearn.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.enroll.RefundRequest;
import fpt.fall23.onlearn.dto.transaction.TransactionFilterStudent;
import fpt.fall23.onlearn.dto.transaction.TransactionFilterTeacher;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.EnrollStatus;
import fpt.fall23.onlearn.enums.TransactionStatus;
import fpt.fall23.onlearn.enums.TransactionType;
import fpt.fall23.onlearn.repository.EnrollRepository;
import fpt.fall23.onlearn.service.*;
import fpt.fall23.onlearn.util.CurrencyConverter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import fpt.fall23.onlearn.repository.TransactionRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        // TODO Auto-generated method stub
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        // TODO Auto-generated method stub
        Optional<Transaction> traOptional = transactionRepository.findById(id);
        if (traOptional.isPresent()) {
            return traOptional.get();
        }
        return null;

    }

//    @Override
//    public List<Transaction> findTransactionByStudentId(TransactionFilterStudent transactionFilterStudent) {
//        return transactionRepository.findTransactionByStudentId(transactionFilterStudent.getStudentId());
//    }

    @Override
    public List<Transaction> getTransactionsByTeacherIdAndDateRange(TransactionFilterTeacher transactionFilterTeacher) {
        Long teacherId = transactionFilterTeacher.getTeacherId();
        LocalDate startDate = transactionFilterTeacher.getStartDate();
        LocalDate endDate = transactionFilterTeacher.getEndDate();

        String nativeQuery = "SELECT * FROM transaction WHERE teacher_id = :teacherId";

        if (startDate != null && endDate != null) {
            LocalDateTime startOfDay = startDate.atStartOfDay();
            LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

            Date startDateConverted = java.sql.Timestamp.valueOf(startOfDay);
            Date endDateConverted = java.sql.Timestamp.valueOf(endOfDay);

            nativeQuery += " AND date_process BETWEEN :startDate AND :endDate";
            nativeQuery += " ORDER by date_process desc";

            Query query = entityManager.createNativeQuery(nativeQuery, Transaction.class);
            query.setParameter("teacherId", teacherId);
            query.setParameter("startDate", startDateConverted);
            query.setParameter("endDate", endDateConverted);

            return query.getResultList();
        } else {
            nativeQuery += " ORDER by date_process desc";
            Query query = entityManager.createNativeQuery(nativeQuery, Transaction.class);
            query.setParameter("teacherId", teacherId);

            return query.getResultList();
        }
    }

    @Override
    public List<Transaction> getTransactionsByStudentIdAndDateRange(TransactionFilterStudent transactionFilterStudent) {
        Long studentId = transactionFilterStudent.getStudentId();
        LocalDate startDate = transactionFilterStudent.getStartDate();
        LocalDate endDate = transactionFilterStudent.getEndDate();

        String nativeQuery = "SELECT * FROM transaction WHERE student_id = :studentId";

        if (startDate != null && endDate != null) {
            LocalDateTime startOfDay = startDate.atStartOfDay();
            LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);

            Date startDateConverted = java.sql.Timestamp.valueOf(startOfDay);
            Date endDateConverted = java.sql.Timestamp.valueOf(endOfDay);

            nativeQuery += " AND date_process BETWEEN :startDate AND :endDate";
            nativeQuery += " ORDER by date_process desc";
            Query query = entityManager.createNativeQuery(nativeQuery, Transaction.class);
            query.setParameter("studentId", studentId);
            query.setParameter("startDate", startDateConverted);
            query.setParameter("endDate", endDateConverted);

            return query.getResultList();
        } else {
            nativeQuery += " ORDER by date_process desc";
            Query query = entityManager.createNativeQuery(nativeQuery, Transaction.class);
            query.setParameter("studentId", studentId);

            return query.getResultList();
        }
    }

    @Override
    public List<Transaction> findTransactionForWithdraw(Long teacherId) {
        return transactionRepository.findTransactionForWithdraw(teacherId);
    }

    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }


    @Autowired
    StudentService studentService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    CourseService courseService;

    @Autowired
    WalletService walletService;


    @Autowired
    EnrollService enrollService;

    @Autowired
    EnrollRepository enrollRepository;
    @Autowired
    SystemConfigService systemConfigService;

    @Override
    @Transactional
    public ResponseDTO<Transaction> doRefund(RefundRequest refundRequest) {


        Student student = studentService.findStudentById(refundRequest.getStudentId());
        Account account = authenticationService.findAccountById(student.getAccount().getId());

        Course course = courseService.getCourseById(refundRequest.getCourseId());

        if (enrollService.checkIsBannedAction(account.getId())) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Bạn đã bị hạn chế hành động do quá số lần hoàn tiền", null);
        }

        Enroll enroll =
                enrollService.findEnrollByStudentAndCourse(student.getId(), course.getId(), EnrollStatus.PROCESSING);
        if (enroll == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm được đăng kí khoá học", null);
        }


        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime requestDate = enroll.getRequestDate().plusDays(7);
        if (currentDate.isAfter(requestDate)) {
//            return new ResponseEntity<>("Can't refund because the finish date", HttpStatus.BAD_REQUEST);
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Quá hạn hoàn tiền", null);
        }


        Transaction parentTransaction = transactionRepository.findTransactionByEnrollIdAndTransactionStatusAndTransactionType(enroll.getId(), TransactionStatus.COMPLETED,
                TransactionType.ENROLLED);
        if (parentTransaction == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy giao dịch", null);
        }

        if (parentTransaction.getTransactionStatus().equals(TransactionStatus.CANCEL) &&
                parentTransaction.getTransactionType().equals(TransactionType.ENROLLED)) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Đã hoàn tiền", null);
        }

        Wallet wallet = walletService.getWalletByAccountId(student.getAccount().getId());
        if (wallet == null) { // Exist wallet
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy ví", null);
        }

        wallet.setAmount(wallet.getAmount() + parentTransaction.getAmount());
        Transaction transaction = new Transaction();
        transaction.setAccountName(account.getProfile().getFirstName() + " " + account.getProfile().getLastName());
        transaction.setAmount(course.getPrice());
        transaction.setDateProcess(LocalDateTime.now());
        transaction.setEnroll(enroll);
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        transaction.setDescription(String.format("Khách hàng %s đã hoàn tiền khoá học %s vào ngày %s",
                account.getProfile().getEmail(), course.getName(), new Date()));
        transaction.setTransactionType(TransactionType.REFUNDED);
        transaction.setParentId(parentTransaction.getId());
        wallet = walletService.saveWallet(wallet);
        transaction.setWallet(wallet);
        transaction.setStudent(student);
        saveTransaction(transaction); //save new Transaction

        parentTransaction.setTransactionStatus(TransactionStatus.CANCEL); //Update status of old transaction
        transaction = saveTransaction(parentTransaction);
        enroll.setStatus(EnrollStatus.REFUNDED);
        enrollService.saveEnroll(enroll);

        SystemConfig systemConfig = systemConfigService.getLastSystemConfig();
        Integer refundTime = enrollRepository.countTotalEnrollRefunded(student.getId(), course.getId());
        if (refundTime >= systemConfig.getRefundedTime()) {
            enroll.setBanned(true);
            enrollService.saveEnroll(enroll);
        }


        return new ResponseDTO<>(HttpStatus.OK.value(), "success", transaction);
    }
}
