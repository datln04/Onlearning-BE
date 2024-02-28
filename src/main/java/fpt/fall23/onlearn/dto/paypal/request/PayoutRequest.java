package fpt.fall23.onlearn.dto.paypal.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PayoutRequest {
    private Long accountId;
    private String amountValue;
}
