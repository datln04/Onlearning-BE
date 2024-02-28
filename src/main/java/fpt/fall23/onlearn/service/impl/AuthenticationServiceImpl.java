package fpt.fall23.onlearn.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import fpt.fall23.onlearn.config.JwtService;
import fpt.fall23.onlearn.config.MailConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.account.request.ChangePasswordRequest;
import fpt.fall23.onlearn.dto.account.request.CreateAccountRequest;
import fpt.fall23.onlearn.dto.account.request.ForgotPasswordRequest;
import fpt.fall23.onlearn.dto.auth.request.AuthenticationRequest;
import fpt.fall23.onlearn.dto.auth.request.GoogleLoginRequest;
import fpt.fall23.onlearn.dto.auth.response.AuthenticationResponse;
import fpt.fall23.onlearn.entity.*;
import fpt.fall23.onlearn.enums.ResponseCode;
import fpt.fall23.onlearn.enums.Role;
import fpt.fall23.onlearn.exception.ApiException;
import fpt.fall23.onlearn.repository.AccountRepository;
import fpt.fall23.onlearn.repository.StaffRepository;
import fpt.fall23.onlearn.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final NetHttpTransport transport;

    private final GsonFactory gsonFactory;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    ProfileService profileService;

    @Value("${google.client-id}")
    private String cliendId;

    private final String STUDENT_DOMAIN = "fpt.edu.vn";

    @Override
    /**
     * Login for admin and event managers
     */
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            var user = accountRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ApiException("Invalid username or password"));

            var jwtToken = jwtService.generateToken(user);
            var jwtRefreshToken = jwtService.generateRefreshToken(user);

            return new AuthenticationResponse().setToken(jwtToken).setRefreshToken(jwtRefreshToken);
        } catch (Exception ex) {
            log.error("Authentication error: {}", ex.getMessage());
        }
        throw new ApiException(ResponseCode.AUTH_ERROR_INVALID_USERNAME_OR_PASSWORD);
    }

    @Override
    public AuthenticationResponse refreshToken(String token, HttpServletRequest request) {
        try {
            Long id = jwtService.extractId(token);

            Optional<Account> userDetails = id != null ? accountRepository.findById(id) : Optional.empty();
            if (userDetails.isPresent() && jwtService.isTokenValid(token, userDetails.get())) {
                String accessToken = jwtService.generateToken(userDetails.get());
                String newRefreshToken = jwtService.generateRefreshToken(userDetails.get());
                return new AuthenticationResponse().setToken(accessToken).setRefreshToken(newRefreshToken);
            }
        } catch (Exception ex) {
            log.error("Refresh token error: {}", ex.getMessage());
        }

        throw new ApiException("Invalid refresh token");
    }

    @Override
    public Optional<Account> getCurrentAuthenticatedAccount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return accountRepository.findByUsername(((UserDetails) principal).getUsername());
        }
        return accountRepository.findByUsername(principal.toString());
    }

    @Override
    public Optional<String> getCurrentAuthentication() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return Optional.of(username);
    }

    @Override
    public List<String> getCurrentAuthenticationRoles() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = new ArrayList<>();
        if (principal instanceof UserDetails) {
            roles = ((UserDetails) principal).getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }
        return roles;
    }

    @Override
    public boolean isAdmin() {
        return getCurrentAuthenticationRoles().stream().anyMatch(role -> Objects.equals(role, Role.ADMIN.toString()));
    }

    @Override
    public AuthenticationResponse googleLogin(GoogleLoginRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
                    .setAudience(Collections.singletonList(cliendId)).build();

            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken == null || idToken.getPayload() == null
                    || StringUtils.isEmpty(idToken.getPayload().getEmail())) {
                throw new ApiException(ResponseCode.INTERNAL_SERVER_ERROR);
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            // Get profile information from payload
            String email = payload.getEmail();
            Optional<Account> accountOpt = accountRepository.findAccountByProfileEmail(email);

//            if (!email.endsWith(STUDENT_DOMAIN) || accountOpt.isEmpty()) {
//                throw new ApiException(ResponseCode.AUTH_ERROR_NOT_PERMITTED);
//            }

            if (accountOpt.isEmpty()) {
                throw new ApiException(ResponseCode.AUTH_ERROR_NOT_PERMITTED);
            }

            Account account = accountOpt.get();
            String token = jwtService.generateToken(account);
            String refreshToken = jwtService.generateRefreshToken(account);
            return new AuthenticationResponse(token, refreshToken);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        throw new ApiException(ResponseCode.AUTH_ERROR_NOT_PERMITTED);
    }

    @Override
    public ResponseDTO<String> disableAccount(Boolean status, Long id, String reason) {
        Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isPresent()) {
//            accountRepository.updateAccountStatus(status, id);
            Account account = accountOpt.get();
            account.setActive(status);
            account.setReason(reason);
            accountRepository.save(account);
            return new ResponseDTO<>(HttpStatus.OK.value(), "success", "success");
        }
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "not found", "fail");

    }

    @Override
    public ResponseDTO<String> enableAccount(Boolean status, Long id) {
        Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setActive(status);
            accountRepository.save(account);
            return new ResponseDTO<>(HttpStatus.OK.value(), "success", "success");
        }
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "not found", "fail");
    }

    @Override
    public Account findAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            return account.get();
        }
        return null;
    }

    @Override
    public Account findAccountByProfileEmail(String email) {
        Optional<Account> account = accountRepository.findByProfileEmail(email);
        if (account.isPresent()) {
            return account.get();
        }
        return null;
    }

    public List<Account> findAccountByRole(Role roleId) {
        return accountRepository.findAccountByRole(roleId);
    }


    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }


    @Override
    public Account saveAccount(Long account) {
        return null;
    }


    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    StudentService studentService;

    @Autowired
    WalletService walletService;
    @Override
    public ResponseDTO<Account> registerAccount(CreateAccountRequest createAccountRequest) {

        Profile profile = profileService.getProfileByEmail(createAccountRequest.getEmail());
        if (profile != null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Tồn tại email", null);
        }
        profile = new Profile();
        BeanUtils.copyProperties(createAccountRequest, profile);
        profile.setAvatar("default.png");
        profile = profileService.saveProfile(profile);
        Optional<Account> accountOpt = accountRepository.findByUsername(createAccountRequest.getUsername());
        if (accountOpt.isPresent()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Tồn tại username", null);
        }
        Account account = new Account();
        BeanUtils.copyProperties(createAccountRequest, account);
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setCreatedAt(OffsetDateTime.now());
        account.setProfile(profile);
        account.setActive(true);
        account.setRole(Role.STUDENT);
        account = accountRepository.save(account);

        Student student = new Student();
        student.setStudentNumber(String.format("STUDENT_%s", account.getId()));
        student.setAccount(account);
        studentService.saveStudent(student);

        Wallet wallet = new Wallet();
        wallet.setAmount(0.0);
        wallet.setAccount(account);
        walletService.saveWallet(wallet);

        return new ResponseDTO<>(HttpStatus.OK.value(), "success", account);
    }


    @Autowired
    TeacherService teacherService;

    @Override
    public ResponseDTO<Account> createTeacherAccount(CreateAccountRequest createAccountRequest) {
        Profile profile = profileService.getProfileByEmail(createAccountRequest.getEmail());
        if (profile != null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Đã tồn tại email", null);
        }
        profile = new Profile();
        BeanUtils.copyProperties(createAccountRequest, profile);
        profile.setAvatar("default.png");
        profile = profileService.saveProfile(profile);
        Optional<Account> accountOpt = accountRepository.findByUsername(createAccountRequest.getUsername());
        if (accountOpt.isPresent()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Tồn tại username", null);
        }
        Account account = new Account();
        BeanUtils.copyProperties(createAccountRequest, account);
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setCreatedAt(OffsetDateTime.now());
        account.setProfile(profile);
        account.setActive(true);
        account.setRole(Role.TEACHER);
        account = accountRepository.save(account);

        Teacher teacher = new Teacher();
        teacher.setTeacherNumber(String.format("TEACHER_%s", account.getId()));
        teacher.setAccount(account);
        teacherService.saveTeacher(teacher);

        Wallet wallet = new Wallet();
        wallet.setAmount(0.0);
        wallet.setAccount(account);
        walletService.saveWallet(wallet);


        return new ResponseDTO<>(HttpStatus.OK.value(), "success", account);
    }

    @Override
    public ResponseDTO<Account> createManagerAccount(CreateAccountRequest createAccountRequest) {
        Profile profile = profileService.getProfileByEmail(createAccountRequest.getEmail());
        if (profile != null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Đã tồn tại email", null);
        }
        profile = new Profile();
        BeanUtils.copyProperties(createAccountRequest, profile);
        profile.setAvatar("default.png");
        profile = profileService.saveProfile(profile);
        Optional<Account> accountOpt = accountRepository.findByUsername(createAccountRequest.getUsername());
        if (accountOpt.isPresent()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Tồn tại username", null);
        }
        Account account = new Account();
        BeanUtils.copyProperties(createAccountRequest, account);
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setCreatedAt(OffsetDateTime.now());
        account.setProfile(profile);
        account.setActive(true);
        account.setRole(Role.STAFF);
        account = accountRepository.save(account);

        Staff staff = new Staff();
        staff.setStaffNumber(String.format("STAFF_%s", account.getId()));
        staff.setAccount(account);
        staffRepository.save(staff);
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", account);
    }

    @Override
    public ResponseDTO<Account> createAdminAccount(CreateAccountRequest createAccountRequest) {
        Profile profile = profileService.getProfileByEmail(createAccountRequest.getEmail());
        if (profile != null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Đã tồn tại email", null);
        }
        profile = new Profile();
        BeanUtils.copyProperties(createAccountRequest, profile);
        profile.setAvatar("default.png");
        profile = profileService.saveProfile(profile);
        Optional<Account> accountOpt = accountRepository.findByUsername(createAccountRequest.getUsername());
        if (accountOpt.isPresent()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Tồn tại username", null);
        }
        Account account = new Account();
        BeanUtils.copyProperties(createAccountRequest, account);
        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setCreatedAt(OffsetDateTime.now());
        account.setProfile(profile);
        account.setActive(true);
        account.setRole(Role.ADMIN);
        account = accountRepository.save(account);
//        Staff staff = new Staff();
//        staff.setStaffNumber(String.format("Admin%s", account.getId()));
//        staff.setAccount(account);
//        staffRepository.save(staff);
        return new ResponseDTO<>(HttpStatus.OK.value(), "success", account);
    }



    @Autowired
    StaffRepository staffRepository;

    @Override
    public ResponseDTO<Account> changePassword(ChangePasswordRequest changePasswordRequest) {


        Optional<Account> accountOpt = getCurrentAuthenticatedAccount();

        if (accountOpt.isEmpty()) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy tài khoản", null);
        }

        Account account = accountOpt.get();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        if (!bCryptPasswordEncoder.matches(oldPassword, account.getPassword())) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Mật khẩu không trùng", null);
        }

        if (!validatePasswordStrength(newPassword)) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Độ dài không đủ (8)", null);
        }

        account.setPassword(bCryptPasswordEncoder.encode(newPassword));
        account = accountRepository.save(account);
        return new ResponseDTO<>(HttpStatus.OK.value(), "Thành công", account);
    }

    // Method to validate password strength (you can define your criteria)
    private boolean validatePasswordStrength(String password) {
        // Implement your password strength validation logic (e.g., length, complexity)
        // Return true if the password meets the criteria, false otherwise
        return password.length() >= 8; // Example: Minimum length of 8 characters
    }

    @Autowired
    EmailService emailService;

    @Override
    public ResponseDTO<Account> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {

        Account account = findAccountByProfileEmail(forgotPasswordRequest.getEmail());
        if (account == null) {
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy tài khoản", null);
        }
        String password = "123456";
        account.setPassword(bCryptPasswordEncoder.encode(password));
        String title = "Phục hồi mật khẩu";
        String content = String.format("Mật khẩu phục hồi của bạn là :%s", password);
        emailService.sendEmail("cunplong.1@gmail.com", account.getProfile().getEmail(), title, content);
        account = accountRepository.save(account);
        return new ResponseDTO<>(HttpStatus.OK.value(), "thành công", account);
    }
}
