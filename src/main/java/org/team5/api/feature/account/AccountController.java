package org.team5.api.feature.account;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.exceptions.UnauthorizedException;

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

    @PostMapping("/accounts/tokens")
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

    @PatchMapping("/account")
    public ResponseEntity<ExtendedAccountDto> editAccount(
            @RequestBody ExtendedAccountDto patchAccount,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token.");
        }

        UUID userId = UUID.fromString(subject);
        if (!userId.equals(patchAccount.getId())) {
            throw new UnauthorizedException("Authenticated users Id does not match target resource.");
        }

        Account updatedAccount = accountService.updateAccount(userId, patchAccount);
        ExtendedAccountDto result = new ExtendedAccountDto(updatedAccount);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/accounts/{id}")
    public void deleteAccount(UUID id) {
        // TODO:
        return;
    }

    @PostMapping("/accounts/admins")
    public ResponseEntity<ExtendedAccountDto> createAdminAccount(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action.");
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
        // TODO: get users own account
        return null;
    }

    @GetMapping("/accounts/{id}")
    public ExtendedAccountDto getAccountAsAdmin(UUID id) {
        // TODO: as admin get a users account by id
        return null;
    }

    @PatchMapping("/accounts/{id}")
    public ExtendedAccountDto editAccountAsAdmin(UUID id, Account account) {
        // TODO: as admin update users account by id
        return null;
    }

    @PutMapping(
            value = "account/profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE //takes in an image file
    )
    public ExtendedAccountDto uploadProfilePicture(UUID id, byte[] profilePicture) {
        // TODO: update a
        return null;
    }
}
