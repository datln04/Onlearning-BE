package fpt.fall23.onlearn.dto.resource;

import fpt.fall23.onlearn.entity.Resource;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {
    private Long id;

    private String content;

    private String name;

    private String resourceType;

    public static ResourceDTO create(Resource resource) {
        return ResourceDTO.builder()
                .id(resource.getId())
                .name(resource.getName())
                .resourceType(resource.getResourceType())
                .content(resource.getContent())
                .build();
    }
}
