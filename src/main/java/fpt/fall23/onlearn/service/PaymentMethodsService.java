package fpt.fall23.onlearn.service;

import java.util.List;

import fpt.fall23.onlearn.entity.PaymentMethods;

public interface PaymentMethodsService {

    List<PaymentMethods> findByAccountId(Long accountId);
    PaymentMethods savePaymentMethods(PaymentMethods paymentMethods);
    Boolean removePaymentMethods(Long id);

}
