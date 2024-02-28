package fpt.fall23.onlearn.dto.paypal.response;

import com.paypal.api.payments.Links;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PaypalPaymentResponse {
    private String id;
    private String state;
    private List<Links> links;
}
