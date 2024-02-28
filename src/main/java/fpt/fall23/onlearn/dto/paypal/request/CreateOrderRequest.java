package fpt.fall23.onlearn.dto.paypal.request;

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
public class CreateOrderRequest {
	private Long accountId;
	private String value;
}
