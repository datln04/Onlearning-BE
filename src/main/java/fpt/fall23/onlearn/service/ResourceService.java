package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Resource;

import java.util.List;

public interface ResourceService {
	
    Resource getResourceById(Long id);
    
    List<Resource> findAllResource();
    
    List<Resource> findResourceByLessonId(Long lessonId);

    Resource saveResource(Resource resource);

}
