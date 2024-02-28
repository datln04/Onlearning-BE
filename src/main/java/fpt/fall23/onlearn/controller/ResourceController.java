package fpt.fall23.onlearn.controller;

import java.util.List;
import java.util.stream.Collectors;

import fpt.fall23.onlearn.dto.resource.ResourceDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.resource.ResourceRequest;
import fpt.fall23.onlearn.dto.resource.ResourceView;
import fpt.fall23.onlearn.entity.Lesson;
import fpt.fall23.onlearn.entity.Resource;
import fpt.fall23.onlearn.service.LessonService;
import fpt.fall23.onlearn.service.ResourceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {
    // Resource getResourceById(Long id);

    // List<Resource> findAllResource();

    // List<Resource> findResourceByLessonId(Long lessonId);

    // Resource saveResource(Resource resource);
    @Autowired
    ResourceService resourceService;

    @GetMapping(path = "/by-id")
    @JsonView(ResourceView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Resource> getResourceById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(resourceService.getResourceById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/by-lesson")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<ResourceDTO>> getResourceByLesson(@RequestParam(name = "lesson_id") Long LessonId) {
        List<ResourceDTO> resources = resourceService.findResourceByLessonId(LessonId)
                .stream().map(ResourceDTO::create)
                .toList();
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(path = "/resources")
    @JsonView(ResourceView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Resource>> getResources() {
        return new ResponseEntity<>(resourceService.findAllResource(), HttpStatus.OK);
    }

    @Autowired
    LessonService lessonService;

    @PostMapping(path = "/save")
    @JsonView(ResourceView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Resource> saveResource(@RequestBody ResourceRequest resourceRequest) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceRequest, resource);
        Lesson lesson = lessonService.getLessonById(resourceRequest.getLessonId());
        if (lesson != null) {
            resource.setLesson(lesson);
        }
        return new ResponseEntity<>(resourceService.saveResource(resource), HttpStatus.OK);
    }

}
