package fpt.fall23.onlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.fall23.onlearn.entity.PaymentMethods;
import java.util.List;


@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, Long>{
    
    List<PaymentMethods> findByAccountId(Long accountId);

}
