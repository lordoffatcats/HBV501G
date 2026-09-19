package org.team5.api.feature.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<ExtendedAccountDto> createAccount(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String username = body.get("username");
        String password = body.get("password");
        ExtendedAccountDto result = new ExtendedAccountDto(
            accountService.createAccount(
                email,
                username,
                password,
                false
            )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    };

    @PostMapping("/accounts/signin")
    public String signIn(String username, String password) {
        // TODO:
        return null;
    }

    @PatchMapping("/accounts/")
    public ExtendedAccountDto editAccount(UUID id, Account account) {
        // TODO:
        return null;
    }

    @DeleteMapping("/account")
    public void deleteAccount(UUID id) {
        // TODO:
        return;
    }

    @PostMapping("/accounts/admin")
    public ExtendedAccountDto createAdminAccount(Account account) {
        // TODO:
        return null;
    }


    @GetMapping("/account")
    public ExtendedAccountDto getAccount() {
        // TODO:
        return null;
    }

    @GetMapping("/accounts/{id}")
    public ExtendedAccountDto getAccountAsAdmin(UUID id) {
        // TODO:
        return null;
    }

    @PatchMapping("accounts/{id}")
    public ExtendedAccountDto editAccountAsAdmin(UUID id, Account account) {
        // TODO:
        return null;
    }

    @PutMapping(
            value = "/account/profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE //takes in an image file
    )
    public ExtendedAccountDto uploadProfilePicture(UUID id, byte[] profilePicture) {
        // TODO:
        return null;
    }
}
