package fpt.fall23.onlearn.repository;

import fpt.fall23.onlearn.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findResourceByLessonId(Long lessonId);
    
}
