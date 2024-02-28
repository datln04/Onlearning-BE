package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.quiz.ViewQuiz;
import fpt.fall23.onlearn.entity.Quiz;
import fpt.fall23.onlearn.entity.SystemConfig;
import fpt.fall23.onlearn.service.SystemConfigService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/system-config")
public class SystemConfigController {


    @Autowired
    SystemConfigService systemConfigService;

    @PostMapping("/save")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<SystemConfig> saveSystemConfig(@RequestBody SystemConfig systemConfig) {
        return new ResponseEntity<>(systemConfigService.saveSystemConfig(systemConfig), HttpStatus.OK);
    }

    @GetMapping("/by-id")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<SystemConfig> getSystemConfigById(@RequestParam(name = "system-config-id") Long id) {
        return new ResponseEntity<>(systemConfigService.getSystemConfigById(id), HttpStatus.OK);
    }

    @GetMapping("/get-last-config")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<SystemConfig> getLastSystemConfigById() {
        return new ResponseEntity<>(systemConfigService.getLastSystemConfig(), HttpStatus.OK);
    }

    @GetMapping("/configs")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<SystemConfig>> getSystemConfigs() {
        return new ResponseEntity<>(systemConfigService.findAllSystemConfigs(), HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<String> removeSystemConfig(@RequestParam("id") Long id) {
        if (systemConfigService.removeSystemConfig(id)) {
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
    }

}
