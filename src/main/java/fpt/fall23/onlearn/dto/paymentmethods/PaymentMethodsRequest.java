package fpt.fall23.onlearn.dto.paymentmethods;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PaymentMethodsRequest {
    private Long id;

    private String methodType;

    private String cardNumber;
    
    private String cardHolderName;
    
    private LocalDate cardExpirationDate;

    private Long accountId;
}
