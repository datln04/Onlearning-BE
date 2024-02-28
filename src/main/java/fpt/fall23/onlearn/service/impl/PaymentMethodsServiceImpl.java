package fpt.fall23.onlearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fpt.fall23.onlearn.entity.PaymentMethods;
import fpt.fall23.onlearn.repository.PaymentMethodsRepository;
import fpt.fall23.onlearn.service.PaymentMethodsService;

@Service
public class PaymentMethodsServiceImpl implements PaymentMethodsService {

    @Autowired
    PaymentMethodsRepository paymentMethodsRepository;

    @Override
    public List<PaymentMethods> findByAccountId(Long accountId) {
        return paymentMethodsRepository.findByAccountId(accountId);
    }

    @Override
    public PaymentMethods savePaymentMethods(PaymentMethods paymentMethods) {
        return paymentMethodsRepository.save(paymentMethods);
    }

    @Override
    public Boolean removePaymentMethods(Long id) {
        Optional<PaymentMethods> paymentMethods = paymentMethodsRepository.findById(id);
        if (paymentMethods.isPresent()) {
            paymentMethodsRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
