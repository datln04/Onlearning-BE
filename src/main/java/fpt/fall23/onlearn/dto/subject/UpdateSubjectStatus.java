package fpt.fall23.onlearn.dto.subject;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UpdateSubjectStatus {
    private Long subjectId;
    private Boolean status;
}
