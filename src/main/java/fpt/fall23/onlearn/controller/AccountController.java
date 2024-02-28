package fpt.fall23.onlearn.controller;

import fpt.fall23.onlearn.config.OpenApiConfig;
import fpt.fall23.onlearn.dto.ResponseDTO;
import fpt.fall23.onlearn.dto.account.request.ChangePasswordRequest;
import fpt.fall23.onlearn.dto.account.request.CreateAccountRequest;
import fpt.fall23.onlearn.dto.account.request.ForgotPasswordRequest;
import fpt.fall23.onlearn.entity.Account;
import fpt.fall23.onlearn.enums.Role;
import fpt.fall23.onlearn.mappings.AccountMapper;
import fpt.fall23.onlearn.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AuthenticationService authenticationService;
    private final AccountMapper mapper;

    @GetMapping("/accounts")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Account>> getAccounts() {
        return new ResponseEntity<>(authenticationService.findAllAccounts(), HttpStatus.OK);
    }

    @GetMapping("/disable")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @Transactional
    public ResponseDTO<String> disableAccount(@RequestParam(name = "account_id") Long id, @RequestParam(name = "reason", defaultValue = "") String reason) {
        return authenticationService.disableAccount(false, id, reason);
    }

    @GetMapping("/enable")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @Transactional
    public ResponseDTO<String> enableAccount(@RequestParam(name = "account_id") Long id) {
        return authenticationService.enableAccount(true, id);
    }

    @GetMapping("/byRoleId")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<List<Account>> getAccountByRoleId(@RequestParam(name = "role_id") Role role) {
        return new ResponseEntity<>(authenticationService.findAccountByRole(role), HttpStatus.OK);
    }

    @PostMapping("/teacher-account")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResponseDTO<Account>> createTeacherAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return new ResponseEntity<>(authenticationService.createTeacherAccount(createAccountRequest),
                HttpStatus.OK);
    }

    @PostMapping("/manager-account")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResponseDTO<Account>> createManagerAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return new ResponseEntity<>(authenticationService.createManagerAccount(createAccountRequest),
                HttpStatus.OK);
    }

    @PostMapping("/admin-account")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ResponseDTO<Account>> createAdminAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return new ResponseEntity<>(authenticationService.createAdminAccount(createAccountRequest),
                HttpStatus.OK);
    }

    @PostMapping("/change-password")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<Account> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return authenticationService.changePassword(changePasswordRequest);
    }

    @PostMapping("/forgot-password")
    public ResponseDTO<Account> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return authenticationService.forgotPassword(forgotPasswordRequest);
    }


    @GetMapping("/profile")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseDTO<Account> viewProfile(@RequestParam(name = "account-id") Long accountId) {
        Account account = authenticationService.findAccountById(accountId);
        if(account == null){
            return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy tài khoản",
                    null);
        }
        return new ResponseDTO<>(HttpStatus.OK.value(), "thành công", authenticationService.findAccountById(accountId));
    }



}
