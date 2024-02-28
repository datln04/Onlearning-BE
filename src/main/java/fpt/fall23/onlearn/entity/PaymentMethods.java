package fpt.fall23.onlearn.entity;

import java.sql.Date;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.paymentmethods.PaymentMethodsView;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonView(PaymentMethodsView.class)
public class PaymentMethods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String methodType;

    private String cardNumber;
    
    private String cardHolderName;
    
    private LocalDate cardExpirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
