package org.team5.api.feature.account;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.exceptions.UnauthorizedException;

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
            @RequestBody CreateAccountRequest request) {
        String email = request.email();
        String username = request.username();
        String password = request.password();

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
    public ResponseEntity<String> signIn(@RequestBody SignInRequest request) {
        String username = request.username();
        String password = request.password();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new BadRequestException("Username and password are required");
        }
        String token = accountService.authenticate(username, password);

        // TODO: For future maybe set browser cookie, instead of just returning raw token.
        return ResponseEntity.ok(token);
    }

    @PatchMapping("/accounts")
    public ResponseEntity<ExtendedAccountDto> editAccount(
            @RequestBody UpdateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token.");
        }

        UUID userId = UUID.fromString(subject);
        if (request.email().isEmpty()) {
            throw new BadRequestException("Email is required");
        }

        Account updatedAccount = accountService.updateAccount(userId, request);
        ExtendedAccountDto result = new ExtendedAccountDto(updatedAccount);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();

        if(subject==null) {
            throw new UnauthorizedException("Invalid token.");
        }
        if(!UUID.fromString(subject).equals(id)) {
            throw new UnauthorizedException("You do not have permission to delete id: "+ id);
        }
        accountService.deleteAccount(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accounts/admins")
    public ResponseEntity<ExtendedAccountDto> createAdminAccount(
            @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action.");
        }

        String email = request.email();
        String username = request.username();
        String password = request.password();
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


    @GetMapping("/accounts")
    public ResponseEntity<ExtendedAccountDto> getAccount(@AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        if(subject==null) {
            throw new UnauthorizedException("Invalid token.");
        }
        Account account = accountService.getAccount(UUID.fromString(subject));
        ExtendedAccountDto accountDto = new ExtendedAccountDto(account);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<ExtendedAccountDto> getAccountAsAdmin(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action.");
        }
        Account account = accountService.getAccount(id);
        ExtendedAccountDto accountDto = new ExtendedAccountDto(account);
        return ResponseEntity.ok(accountDto);
    }

    @PatchMapping("/accounts/{id}")
    public ResponseEntity<ExtendedAccountDto> editAccountAsAdmin(
            @PathVariable UUID id,
            @RequestBody UpdateAccountRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action.");
        }
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token.");
        }

        if (request.email().isEmpty()) {
            throw new BadRequestException("Email is required");
        }

        Account updatedAccount = accountService.updateAccount(id, request);
        ExtendedAccountDto result = new ExtendedAccountDto(updatedAccount);
        return ResponseEntity.ok(result);
    }

    @PutMapping(
            value = "accounts/profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE //takes in an image file
    )
    public ExtendedAccountDto uploadProfilePicture(UUID id, Base64 profilePicture) {
        // TODO: update a
        return null;
    }
}
