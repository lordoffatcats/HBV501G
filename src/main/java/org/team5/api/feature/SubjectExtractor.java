package org.team5.api.feature;

import org.springframework.security.oauth2.jwt.Jwt;
import org.team5.api.exceptions.UnauthorizedException;

import java.util.UUID;

public class SubjectExtractor {
    public static UUID GetSubject(Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID id;
        try {
            id = UUID.fromString(subject);
        } catch (IllegalArgumentException exception) {
            throw new UnauthorizedException("Invalid token");
        }

        return id;
    }
}
