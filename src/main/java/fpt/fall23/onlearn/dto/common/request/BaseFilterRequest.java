package fpt.fall23.onlearn.dto.common.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fpt.fall23.onlearn.util.TrimString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseFilterRequest<T> {
    @JsonDeserialize(using = TrimString.class)
    private String sortBy;

    private Sort.Direction sortDir = Sort.Direction.ASC;

    public Sort getSort() {

        return sortBy != null
                ? Sort.by(sortDir, sortBy)
                : Sort.unsorted();
    }

    public abstract Specification<T> getSpecification();
}
