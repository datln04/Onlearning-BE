package fpt.fall23.onlearn.controller;

import fpt.fall23.onlearn.util.CurrencyConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.wallet.WalletRequest;
import fpt.fall23.onlearn.dto.wallet.WalletView;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.Wallet;
import fpt.fall23.onlearn.service.AuthenticationService;
import fpt.fall23.onlearn.service.WalletService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    
    @Autowired
    WalletService walletService;

    @GetMapping("/by-account")
    @JsonView(WalletView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Wallet> getByAccountId(@RequestParam(name = "account_id") Long accountId){
        Wallet wallet = walletService.getWalletByAccountId(accountId);
//        wallet.setAmount(CurrencyConverter.convertUSDtoVND(wallet.getAmount() == null ? 0.0 : wallet.getAmount()));
        Wallet response = wallet != null ? wallet : new Wallet();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/save")
    @JsonView(WalletView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Wallet> saveWallet(@RequestBody WalletRequest walletRequest){
        Wallet wallet = new Wallet();
        BeanUtils.copyProperties(walletRequest, wallet);
        Account account = authenticationService.findAccountById(walletRequest.getAccountId());
        if(account != null){
            wallet.setAccount(account);
        }
        return new ResponseEntity<>(walletService.saveWallet(wallet), HttpStatus.OK);
    }

}
