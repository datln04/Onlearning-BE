package fpt.fall23.onlearn.controller;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {


    @Autowired
    DashboardService dashboardService;

    @GetMapping("/total-enroll-by-month")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<Double> getTotalEnrolledByMont(@RequestParam(name = "month") Integer month) {
        return new ResponseEntity<>(dashboardService.totalEnrolledByMonth(month), HttpStatus.OK);
    }


    @GetMapping("/total-enroll-done")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @Operation(description = "Lay tong khong tru hoa hong")
    public ResponseEntity<List<Map<String, Double>>> getTotalEnrolledDone(@RequestParam(name = "year") Integer year) {
        return new ResponseEntity<>(dashboardService.getTotalEnrolledByYear(year), HttpStatus.OK);
    }

    @GetMapping("/total-commision-fee")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @Operation(description = "Lay tong hoa hong cua hoc sinh dang ky")
    public ResponseEntity<List<Map<String, Double>>> getTotalCommisionfee(@RequestParam(name = "year") Integer year) {
        return new ResponseEntity<>(dashboardService.getTotalCommisionFeeByYear(year), HttpStatus.OK);
    }

    @GetMapping("/total-request-service-charge")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @Operation(description = "Lay tong tien phi thu tu request cua teacher")
    public ResponseEntity<List<Map<String, Double>>> getTotalServiceCharge(@RequestParam(name = "year") Integer year) {
        return new ResponseEntity<>(dashboardService.getTotalRequestFeeByYear(year), HttpStatus.OK);
    }
}
