package fpt.fall23.onlearn.dto.paypal.response;

import com.paypal.api.payments.Links;
import com.paypal.orders.LinkDescription;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class OrderResponse {
    private String id;
    private String status;
    private List<LinkDescriptionResponse> links;
}
