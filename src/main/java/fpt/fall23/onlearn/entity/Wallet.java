package fpt.fall23.onlearn.entity;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.dto.paymenthistory.PaymentHistoryView;
import fpt.fall23.onlearn.dto.transaction.TransactionView;
import fpt.fall23.onlearn.dto.wallet.WalletView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wallet")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonView(WalletView.class)
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({PaymentHistoryView.class, WalletView.class, TransactionView.class})
    private Long id;

    private Double amount;

    private String bankNumber;

    private String bankName;

    @OneToOne(fetch = FetchType.LAZY)
    private Account account;

    private String walletType;

}
