package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Wallet;


public interface WalletService {
    
    Wallet getWalletByAccountId(Long accountId);
    Wallet saveWallet(Wallet wallet);
    Wallet getWalletById(Long id);
}
