package fpt.fall23.onlearn.mappings.impl;

import fpt.fall23.onlearn.dto.account.response.AccountDetailResponse;
import fpt.fall23.onlearn.dto.account.response.AccountResponse;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.entity.Staff;
import fpt.fall23.onlearn.entity.Student;
import fpt.fall23.onlearn.entity.Teacher;
import fpt.fall23.onlearn.enums.Role;
import fpt.fall23.onlearn.mappings.AccountMapper;
import fpt.fall23.onlearn.service.StaffService;
import fpt.fall23.onlearn.service.StudentService;
import fpt.fall23.onlearn.service.TeacherService;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapperImpl implements AccountMapper {

	private final PasswordEncoder passwordEncoder;

//    @Override
//    public Account toEntity(CreateEventManagerRequest request) {
//        return new Account()
//                .setUsername(request.getUsername())
//                .setPassword(passwordEncoder.encode(request.getPassword()))
//                .setName(request.getName())
//                .setEmail(request.getEmail())
//                .setPhone(request.getPhone())
//                .setRole(Role.EVENT_MANAGER);
//    }

	@Override
	public AccountResponse toResponse(Account account) {
		if (account == null) {
			return null;
		}

		return new AccountResponse().setId(account.getId()).setUsername(account.getUsername())
				.setName(
						String.format("%s %s", account.getProfile().getFirstName(), account.getProfile().getLastName()))
				.setEmail(account.getProfile().getEmail()).setPhone(account.getProfile().getPhone())
				.setRole(account.getRole()).setDescription(account.getProfile().getDescription())
				.setUpdatedAt(account.getUpdatedAt()).setCreatedAt(account.getCreatedAt())
				.setAvatar(account.getProfile().getAvatar());
	}

	@Autowired
	StudentService studentService;

	@Autowired
	TeacherService teacherService;

	@Autowired
	StaffService staffService;

	@Override
	public AccountDetailResponse toDetailResponse(Account account) {
		if (account == null) {
			return null;
		}

		Student student = null;
		Teacher teacher = null;
		Staff staff = null;
		if (account.getRole().equals(Role.STAFF)) {
			staff = staffService.getStaffByAccountId(account.getId());
		} else if (account.getRole().equals(Role.TEACHER)) {
			teacher = teacherService.getTeacherByAccountId(account.getId());
		} else if (account.getRole().equals(Role.STUDENT)) {
			student = studentService.findStudentByAccountId(account.getId());
		}

		return new AccountDetailResponse().setId(account.getId()).setUsername(account.getUsername())
				.setName(
						String.format("%s %s", account.getProfile().getFirstName(), account.getProfile().getLastName()))
				.setEmail(account.getProfile().getEmail()).setPhone(account.getProfile().getPhone())
				.setRole(account.getRole()).setUpdatedAt(account.getUpdatedAt()).setCreatedAt(account.getCreatedAt())
				.setDescription(account.getProfile().getDescription()).setAvatar(account.getProfile().getAvatar())
				.setStaffId(staff != null ? Integer.parseInt(staff.getId().toString()) : null)
				.setTeacherId(teacher != null ? Integer.parseInt(teacher.getId().toString()) : null)
				.setStudentId(student != null ? Integer.parseInt(student.getId().toString()) : null);
	}

}
