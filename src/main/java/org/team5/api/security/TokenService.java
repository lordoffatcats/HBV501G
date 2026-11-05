package org.team5.api.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.team5.api.feature.account.Account;
import org.team5.api.feature.account.AccountRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TokenService {
    private final JwtEncoder encoder;
    private final AccountRepository accountRepository;

    public TokenService(JwtEncoder encoder, AccountRepository accountRepository) {
        this.encoder = encoder;
        this.accountRepository = accountRepository;
    }

    public String createToken(Authentication auth) {
        Instant now = Instant.now();

        Account account = accountRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        List<String> roles = auth.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .filter(role -> role.equals("USER") || role.equals("ADMIN"))
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(account.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plus(4, ChronoUnit.HOURS))
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }
}
