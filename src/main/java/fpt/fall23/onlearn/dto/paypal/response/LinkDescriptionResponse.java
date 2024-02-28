package fpt.fall23.onlearn.dto.paypal.response;

import com.paypal.http.annotations.SerializedName;
import com.paypal.orders.LinkSchema;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class LinkDescriptionResponse {
    private String href;
    private String method;
    private String rel;
}
