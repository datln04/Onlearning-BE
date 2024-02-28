package fpt.fall23.onlearn.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.paymentmethods.PaymentMethodsRequest;
import fpt.fall23.onlearn.dto.paymentmethods.PaymentMethodsView;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.PaymentMethods;
import fpt.fall23.onlearn.service.AuthenticationService;
import fpt.fall23.onlearn.service.PaymentMethodsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/payment_method")
public class PaymentMethodController {

    // getByAccount
    // save
    // delete

    @Autowired
    PaymentMethodsService paymentMethodsService;

    @GetMapping(path = "/by-account")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @JsonView(PaymentMethodsView.class)
    public ResponseEntity<List<PaymentMethods>> getByAccount(@RequestParam(name = "account_id") Long accountId) {
        return new ResponseEntity<>(paymentMethodsService.findByAccountId(accountId), HttpStatus.OK);
    }

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping(path = "/save")
    @JsonView(PaymentMethodsView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<PaymentMethods> savePaymentMethod(@RequestBody PaymentMethodsRequest paymentMethodsRequest) {
        PaymentMethods paymentMethods = new PaymentMethods();
        BeanUtils.copyProperties(paymentMethodsRequest, paymentMethods);
        Account account = authenticationService.findAccountById(paymentMethodsRequest.getAccountId());
        if (account != null) {
            paymentMethods.setAccount(account);
        }
        return new ResponseEntity<>(paymentMethodsService.savePaymentMethods(paymentMethods), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<String> deletePaymentMethod(@RequestParam(name = "method_id") Long methodId) {
        if (paymentMethodsService.removePaymentMethods(methodId)) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<>("FAIL", HttpStatus.OK);
    }

}