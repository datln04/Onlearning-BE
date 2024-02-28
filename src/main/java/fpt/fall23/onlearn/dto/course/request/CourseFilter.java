package fpt.fall23.onlearn.dto.course.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseFilter {
   private String value;
   private Double minPrice;
   private Double maxPrice;
}
