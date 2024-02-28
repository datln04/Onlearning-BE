package fpt.fall23.onlearn.data;

import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.Role;
import fpt.fall23.onlearn.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(value = 2)
public class AccountSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;

    private final StaffRepository staffRepository;

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    private final PasswordEncoder passwordEncoder;

    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            Profile admin = new Profile();
            admin.setFirstName("admin");
            admin.setLastName("account");
            admin.setEmail("admin@gmail.com");
            admin.setAvatar("default.png");
            admin.setPhone("0984065979");
            admin.setAddress("");
            admin.setDescription("");
            admin = profileRepository.save(admin);
            Account account = new Account()
                    .setUsername("admin")
                    .setPassword(passwordEncoder.encode("admin@"))
                    .setRole(Role.ADMIN)
                    .setProfile(admin);
            accountRepository.save(account);
        }

        if (staffRepository.count() == 0) {

            Profile manager = new Profile();
            manager.setFirstName("manager");
            manager.setLastName("account");
            manager.setEmail("manager@gmail.com");
            manager.setAvatar("default.png");
            manager.setPhone("0984065979");
            manager.setAddress("");
            manager.setDescription("");
            manager = profileRepository.save(manager);
            Account account2 = new Account()
                    .setUsername("manager")
                    .setPassword(passwordEncoder.encode("manager"))
                    .setRole(Role.STAFF)
                    .setProfile(manager);
            account2 = accountRepository.save(account2);

            Staff staff_account = new Staff();
            staff_account.setAccount(account2);
            staff_account.setStaffNumber("manager001");
            staffRepository.save(staff_account);
        }

        if (teacherRepository.count() == 0) {
            Profile teacher = new Profile();
            teacher.setFirstName("teacher");
            teacher.setLastName("teacher");
            teacher.setEmail("teacher@gmail.com");
            teacher.setAvatar("default.png");
            teacher.setPhone("0984065979");
            teacher.setAddress("");
            teacher.setDescription("");
            teacher = profileRepository.save(teacher);
            Account account3 = new Account()
                    .setUsername("teacher")
                    .setPassword(passwordEncoder.encode("teacher"))
                    .setRole(Role.TEACHER)
                    .setProfile(teacher);
            account3 = accountRepository.save(account3);
            Teacher teacher_account = new Teacher();
            teacher_account.setAccount(account3);
            teacher_account.setTeacherNumber("teacher001");
            teacherRepository.save(teacher_account);
        }

        if (studentRepository.count() == 0) {
            Profile student = new Profile();
            student.setFirstName("student");
            student.setLastName("student");
            student.setEmail("student@gmail.com");
            student.setAvatar("default.png");
            student.setPhone("0984065979");
            student.setAddress("");
            student.setDescription("");
            student = profileRepository.save(student);
            Account account3 = new Account()
                    .setUsername("student")
                    .setPassword(passwordEncoder.encode("student"))
                    .setRole(Role.STUDENT)
                    .setProfile(student);
            account3 = accountRepository.save(account3);
            Student student_account = new Student();
            student_account.setAccount(account3);
            student_account.setStudentNumber("student001");
            studentRepository.save(student_account);
        }
    }
}
