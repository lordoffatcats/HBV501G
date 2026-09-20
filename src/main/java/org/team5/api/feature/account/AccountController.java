package org.team5.api.feature.account;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.ForbiddenException;

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

        if (email == null || email.isBlank() || username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BadRequestException("Email, username and password are required");
        }

        ExtendedAccountDto result = new ExtendedAccountDto(
            accountService.createAccount(
                email,
                username,
                password,
                false
            )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/accounts/signin")
    public ResponseEntity<String> signIn(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BadRequestException("Username and password are required");
        }
        String token = accountService.authenticate(username, password);

        // TODO: For future maybe set browser cookie, instead of just returning raw token.
        return ResponseEntity.ok(token);
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
    public ResponseEntity<ExtendedAccountDto> createAdminAccount(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }

        String email = body.get("email");
        String username = body.get("username");
        String password = body.get("password");
        if (email == null || email.isBlank() || username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BadRequestException("Email, username and password are required");
        }
        ExtendedAccountDto result = new ExtendedAccountDto(
                accountService.createAccount(
                        email,
                        username,
                        password,
                        true
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
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
