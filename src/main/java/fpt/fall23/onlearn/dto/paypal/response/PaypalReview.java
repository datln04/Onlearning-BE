package fpt.fall23.onlearn.dto.paypal.response;

import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Transaction;

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
public class PaypalReview {
	private PayerInfo payerInfo;
	private Transaction transaction;
}
