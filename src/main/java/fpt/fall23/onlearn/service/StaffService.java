package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.entity.Staff;

import java.util.Optional;

public interface StaffService {
    
	Staff getStaffById(Long id);
	
	Optional<Staff> saveManager(Staff staff);
    
    Staff getStaffByAccountId(Long accountId);
}
