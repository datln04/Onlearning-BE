package fpt.fall23.onlearn.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.fall23.onlearn.entity.Wallet;
import fpt.fall23.onlearn.repository.WalletRepository;
import fpt.fall23.onlearn.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    WalletRepository walletRepository;

    @Override
    public Wallet getWalletByAccountId(Long accountId) {
        return walletRepository.findWalletByAccountId(accountId);
    }

    @Override
    public Wallet saveWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet getWalletById(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()) {
            return wallet.get();
        }
        return null;
    }

}
