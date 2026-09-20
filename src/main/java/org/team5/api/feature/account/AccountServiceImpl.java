package org.team5.api.feature.account;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.UnauthorizedException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl  implements AccountService {
    private final AccountRepository accountRepository;
    private final JwtEncoder jwtEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, JwtEncoder jwtEncoder) {
        this.accountRepository = accountRepository;
        this.jwtEncoder = jwtEncoder;
    }

    public Account createAccount(String email, String username, String password, boolean isAdmin) {
        Optional<Account> existingAccount = accountRepository.findByUsername(username);
        if (existingAccount.isPresent()) {
            throw new BadRequestException("Username already exists");
        }
        return accountRepository.save(new Account(email, username, password, isAdmin));
    };

    public String authenticate(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isEmpty()) {
            throw new UnauthorizedException("Invalid username or password");
        }

        Account existingAccount = account.get();

        if (!existingAccount.getPassword().equals(password)) {
            throw new UnauthorizedException("Invalid username or password");
        }

        return createJwt(existingAccount);
    };

    private String createJwt(Account account) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(account.getId().toString())
                .claim("username", account.getUsername())
                .claim("isAdmin",account.isAdmin())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    public Account getAccount(UUID id) {
        // TODO:
        return null;
    };

    public Account updateAccount(UUID id, String email) {
        // TODO:
        return null;
    };

    public void deleteAccount(UUID id) {
        // TODO:
    };

    public Account updateProfilePicture(UUID id, byte[] profilePicture) {
        // TODO:
        return null;
    };
}
