package fpt.fall23.onlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fpt.fall23.onlearn.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    Wallet findWalletByAccountId(Long accountId);

}
