package org.team5.api.feature.account;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.feature.SubjectExtractor;

import java.util.UUID;

@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<ExtendedAccountDto> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        ExtendedAccountDto result = new ExtendedAccountDto(
            accountService.createAccount(
                request.email,
                request.username,
                request.password,
                false
            )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/accounts/tokens")
    public ResponseEntity<String> signIn(@Valid @RequestBody LoginRequest request) {
        String token = accountService.authenticate(request.username, request.password);
        return ResponseEntity.ok(token);
    }

    @PatchMapping("/accounts")
    public ResponseEntity<ExtendedAccountDto> editAccount(
            @Valid @RequestBody UpdateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = SubjectExtractor.GetSubject(jwt);
        Account updatedAccount = accountService.updateAccount(userId, request.email);
        ExtendedAccountDto result = new ExtendedAccountDto(updatedAccount);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/accounts/{id}")
    public void deleteAccount(@PathVariable UUID id) {
        // TODO:
    }

    @PostMapping("/accounts/admins")
    public ResponseEntity<ExtendedAccountDto> createAdminAccount(
            @Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action.");
        }

        ExtendedAccountDto result = new ExtendedAccountDto(
            accountService.createAccount(
                request.email,
                request.username,
                request.password,
                true
            )
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @GetMapping("/accounts")
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
            value = "accounts/profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE //takes in an image file
    )
    public ExtendedAccountDto uploadProfilePicture(UUID id, byte[] profilePicture) {
        // TODO: update a
        return null;
    }
}
