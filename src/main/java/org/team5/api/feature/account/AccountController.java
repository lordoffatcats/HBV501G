package org.team5.api.feature.account;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.feature.SubjectExtractor;

import java.util.Base64;
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

    @PutMapping("/accounts")
    public ResponseEntity<ExtendedAccountDto> editAccount(
            @Valid @RequestBody UpdateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = SubjectExtractor.GetSubject(jwt);
        Account updatedAccount = accountService.updateAccount(userId, request.email);
        ExtendedAccountDto result = new ExtendedAccountDto(updatedAccount);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        boolean isAdmin = Boolean.TRUE.equals(jwt.getClaim("isAdmin"));
        UUID userId = SubjectExtractor.GetSubject(jwt);
        if (!isAdmin && !userId.equals(id)) {
            throw new ForbiddenException("You can only delete your own account");
        }

        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<ExtendedAccountDto> getAccount(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = SubjectExtractor.GetSubject(jwt);
        ExtendedAccountDto result = new ExtendedAccountDto(accountService.getAccount(userId));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<ExtendedAccountDto> getAccountAsAdmin(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }

        ExtendedAccountDto result = new ExtendedAccountDto(accountService.getAccount(id));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<ExtendedAccountDto> editAccountAsAdmin(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }

        Account updatedAccount = accountService.updateAccount(id, request.email);
        ExtendedAccountDto result = new ExtendedAccountDto(updatedAccount);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/accounts/profile-picture")
    public ResponseEntity<ExtendedAccountDto> uploadProfilePicture(
            @Valid @RequestBody UploadProfilePictureRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = SubjectExtractor.GetSubject(jwt);
        byte[] imageBytes;

        try {
            imageBytes = Base64.getDecoder().decode(request.profilePicture);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid Base64 image");
        }

        ExtendedAccountDto result = new ExtendedAccountDto(accountService.updateProfilePicture(userId, imageBytes));
        return ResponseEntity.ok(result);
    }
}
