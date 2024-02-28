package fpt.fall23.onlearn.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionFilterTeacher {
    private Long teacherId;
    private LocalDate startDate;
    private LocalDate endDate;

}
