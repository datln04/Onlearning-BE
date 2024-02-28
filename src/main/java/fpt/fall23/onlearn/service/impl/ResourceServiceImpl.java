package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Resource;
import fpt.fall23.onlearn.repository.ResourceRepository;
import fpt.fall23.onlearn.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id).get();
    }

    @Override
    public List<Resource> findAllResource() {
        return resourceRepository.findAll();
    }

    @Override
    public List<Resource> findResourceByLessonId(Long lessonId) {
        return resourceRepository.findResourceByLessonId(lessonId);
    }

    @Override
    public Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }
}
