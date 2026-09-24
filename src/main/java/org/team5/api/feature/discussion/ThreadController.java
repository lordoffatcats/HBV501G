package org.team5.api.feature.discussion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.UnauthorizedException;

import java.util.Map;
import java.util.UUID;

@RestController
public class ThreadController {
    private final ThreadService threadService;

    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @PostMapping("/courses/{courseId}/threads")
    public ResponseEntity<ThreadDto> createThread(
            @PathVariable UUID courseId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID authorId;
        try {
            authorId = UUID.fromString(subject);
        } catch (IllegalArgumentException exception) {
            throw new UnauthorizedException("Invalid token");
        }

        String title = body.get("title");
        String content = body.get("content");

        if (title == null || title.isBlank()) {
            throw new BadRequestException("Title is required");
        }
        if (title.length() > 100) {
            throw new BadRequestException("Title must not exceed 100 characters");
        }
        if (content == null || content.isBlank()) {
            throw new BadRequestException("Content is required");
        }

        Thread thread = threadService.createThread(
                courseId,
                authorId,
                title,
                content
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ThreadDto(thread));
    }
}
