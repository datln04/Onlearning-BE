package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

    @Query(value = "select s, m, a, p from Subject s " +
            "left join Staff m on s.staff.id = m.id " +
            "left join Account a on m.account.id = a.id " +
            "left join Profile p on a.profile.id = p.id ")
    List<Subject> findAllSubject();

    @Query(value = "select s, m, a, p from Subject s " +
            "left join Staff m on s.staff.id = m.id " +
            "left join Account a on m.account.id = a.id " +
            "left join Profile p on a.profile.id = p.id " +
            "where s.id = :id")
    Optional<Subject> findSubjectById(Long id);

    @Query(value = "select s, m, a, p from Subject s " +
            "left join Staff m on s.staff.id = m.id " +
            "left join Account a on m.account.id = a.id " +
            "left join Profile p on a.profile.id = p.id " +
            "where s.name like %:name% ")
    List<Subject> findAllSubjectByName(String name);

}
