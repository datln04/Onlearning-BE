package fpt.fall23.onlearn.service;

import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.account.request.ChangePasswordRequest;
import fpt.fall23.onlearn.dto.account.request.CreateAccountRequest;
import fpt.fall23.onlearn.dto.account.request.ForgotPasswordRequest;
import fpt.fall23.onlearn.dto.auth.request.AuthenticationRequest;
import fpt.fall23.onlearn.dto.auth.request.GoogleLoginRequest;
import fpt.fall23.onlearn.dto.auth.response.AuthenticationResponse;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.enums.Role;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface AuthenticationService {
    AuthenticationResponse authentication(AuthenticationRequest request);

    AuthenticationResponse refreshToken(String token, HttpServletRequest request);

    Optional<Account> getCurrentAuthenticatedAccount();

    Optional<String> getCurrentAuthentication();

    List<String> getCurrentAuthenticationRoles();

    boolean isAdmin();

    AuthenticationResponse googleLogin(GoogleLoginRequest request);
    
    
    ResponseDTO<String> disableAccount(Boolean status, Long id, String reason);

    ResponseDTO<String> enableAccount(Boolean status, Long id);


    Account findAccountById(Long id);

    Account findAccountByProfileEmail(String email);

    List<Account> findAccountByRole(Role role);

    List<Account> findAllAccounts();


    Account saveAccount(Long account);

    ResponseDTO<Account> registerAccount(CreateAccountRequest createAccountRequest);

    ResponseDTO<Account> createTeacherAccount(CreateAccountRequest createAccountRequest);

    ResponseDTO<Account> createManagerAccount(CreateAccountRequest createAccountRequest);

    ResponseDTO<Account> createAdminAccount(CreateAccountRequest createAccountRequest);


    ResponseDTO<Account> changePassword(ChangePasswordRequest changePasswordRequest);


    ResponseDTO<Account> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);



}
