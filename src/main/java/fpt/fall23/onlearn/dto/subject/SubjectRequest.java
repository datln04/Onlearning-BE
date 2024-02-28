package fpt.fall23.onlearn.dto.subject;

import java.time.LocalDate;
import java.util.Date;
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
public class SubjectRequest {
	
	    private Long id;
	    private String name;
	    private String description;
	    private LocalDate createDate;
	    private double minPrice;
	    private Boolean status;
	    private Integer staffId;

}
