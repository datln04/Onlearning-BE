package fpt.fall23.onlearn.dto.enroll;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefundRequest {
    private Long studentId;
    private Long courseId;
//    private Long transactionId;
}
