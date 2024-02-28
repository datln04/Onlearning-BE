package fpt.fall23.onlearn.service.impl;

import fpt.fall23.onlearn.entity.Staff;
import fpt.fall23.onlearn.repository.StaffRepository;
import fpt.fall23.onlearn.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    StaffRepository staffRepository;
    @Override
    public Optional<Staff> saveManager(Staff staff) {
        return Optional.of(staffRepository.save(staff));
    }
	@Override
	public Staff getStaffById(Long id) {
		Optional<Staff> staff = staffRepository.findById(id);
		if(staff.isPresent()) {
			return staff.get();
		}
		return null;
	}
	@Override
	public Staff getStaffByAccountId(Long accountId) {
		Optional<Staff> staff = staffRepository.findStaffByAccountId(accountId);
		if(!staff.isEmpty() && staff.isPresent()) {
			return staff.get();
		}
		return null;
	}
	
	
}
