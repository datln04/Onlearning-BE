package fpt.fall23.onlearn.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.device.DeviceRequest;
import fpt.fall23.onlearn.dto.device.DeviceView;
import fpt.fall23.onlearn.dto.lesson.LessonView;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.Device;
import fpt.fall23.onlearn.service.AuthenticationService;
import fpt.fall23.onlearn.service.DeviceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/byAccountId")
    @JsonView(DeviceView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Device>> getDeviceByAccountId(@RequestParam(name = "accountId") Long accountId){
        return new ResponseEntity<>(deviceService.getDeviceByAccountId(accountId), HttpStatus.OK);
    }

    @PostMapping("/save")
    @JsonView(DeviceView.class)
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Device> getDeviceByAccountId(@RequestBody DeviceRequest deviceRequest){
        Device device = new Device();
        BeanUtils.copyProperties(deviceRequest, device);
        Account account = authenticationService.findAccountById(deviceRequest.getAccountId());
        device.setAccount(account);

        Device exist = deviceService.getDeviceByTokenAndAccountId(deviceRequest.getToken(), deviceRequest.getAccountId());
        if(exist == null){
            return new ResponseEntity<>(deviceService.save(device), HttpStatus.OK);
        }
        return new ResponseEntity<>(exist, HttpStatus.OK);
    }
}
