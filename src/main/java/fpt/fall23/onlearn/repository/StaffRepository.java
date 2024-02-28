package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Staff;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query(value = "select m,a,p from Staff m " +
            "left join Account a on m.account.id = a.id " +
            "left join Profile p on a.profile.id = p.id " +
            "left join Subject s on m.id = s.staff.id " +
            "where s.id = :subjectId")
    Staff findStaffBySubjectId(Long subjectId);
    
   Optional<Staff> findStaffByAccountId(Long accountId);
   
}
