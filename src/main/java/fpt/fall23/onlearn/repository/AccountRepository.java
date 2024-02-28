package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    @Query(value = "select a from Account a inner join a.profile p where LOWER(a.username) = LOWER(:username) and a.active = true")
    Optional<Account> findByUsername(String username);

    Optional<Account> findAccountByProfileEmail(String email);

//    Optional<Account> findFirstByCode(String code);
    
    @Query(value = "update Account a set a.active = :active where a.id = :accountId")
    Long updateAccountStatus(Boolean active, Long accountId);

    @Query(value = "select a from Account a inner join a.profile p where p.email = :email")
    Optional<Account> findByProfileEmail(String email);

    List<Account> findAccountByRole(Role role);
}
