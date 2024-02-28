package fpt.fall23.onlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fpt.fall23.onlearn.entity.Staff;
import fpt.fall23.onlearn.service.StaffService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {
	
	@Autowired
	StaffService staffService;
	
	@GetMapping(path = "/byId")
	
	public ResponseEntity<Staff> getStaffById(@RequestParam(name = "staff_id") Long staffId){
		return new ResponseEntity<Staff>(staffService.getStaffById(staffId), HttpStatus.OK);
	}

}
