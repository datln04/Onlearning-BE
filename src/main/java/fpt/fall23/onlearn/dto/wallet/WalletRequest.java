package fpt.fall23.onlearn.dto.wallet;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class WalletRequest {

    private Long id;

    private Double amount;
    
    private String bankNumber;

    private String bankName;

    private Long accountId;

    @Schema(description = "Type is STUDENT or TEACHER")
    private String walletType;
}
