package fpt.fall23.onlearn.dto.resource;

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
public class ResourceRequest {
    private Long id;
    private String content;
    private String name;
    private String resourceType;
    private Long lessonId;
}
