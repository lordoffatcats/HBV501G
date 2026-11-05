package org.team5.api.feature.account;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.feature.SubjectExtractor;
import org.team5.api.security.TokenResponse;
import org.team5.api.security.TokenService;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AccountController(AccountService accountService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
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
    public TokenResponse signIn(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        request.username, request.password));

        return new TokenResponse(tokenService.createToken(auth), "14.400"/*4 hours*/);
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
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        UUID userId = SubjectExtractor.GetSubject(jwt);
        if (!isAdmin && !userId.equals(id)) {
            throw new ForbiddenException("You can only delete your own account");
        }

        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accounts/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExtendedAccountDto> createAdminAccount(
            @Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        /* I think @PreAuthorize("hasRole('ADMIN')") makes this check redundant now
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action.");
        }
         */

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExtendedAccountDto> getAccountAsAdmin(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        /* if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        } */

        ExtendedAccountDto result = new ExtendedAccountDto(accountService.getAccount(id));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/accounts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExtendedAccountDto> editAccountAsAdmin(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        /*if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }*/

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
