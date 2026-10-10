package org.team5.api.feature.discussion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
            @AuthenticationPrincipal Jwt jwt
    ) {
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

    @GetMapping("/threads/{id}")
    public ResponseEntity<ThreadDto> getThread(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID viewerId;
        try {
            viewerId = UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException exception) {
            throw new UnauthorizedException("Invalid token");
        }

        Thread thread = threadService.getThread(id, viewerId);

        return ResponseEntity.ok(new ThreadDto(thread));
    }

    @PatchMapping("/threads/{id}")
    public ResponseEntity<ThreadDto> editThread(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID authorId;
        try {
            authorId = UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException exception) {
            throw new UnauthorizedException("Invalid token");
        }

        String title = body.get("title");
        String content = body.get("content");

        if (title == null && content == null) {
            throw new BadRequestException("Title or content is required");
        }

        if (title != null) {
            if (title.isBlank()) {
                throw new BadRequestException("Title is required");
            }

            if (title.length() > 100) {
                throw new BadRequestException("Title must not exceed 100 characters");
            }
        }

        if (content != null && content.isBlank()) {
            throw new BadRequestException("Content is required");
        }

        Thread thread = threadService.updateThread(
                id,
                authorId,
                title,
                content
        );

        return ResponseEntity.ok(new ThreadDto(thread));
    }

    @DeleteMapping("/threads/{id}")
    public ResponseEntity<Void> deleteThread(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null || jwt.getSubject() == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID authorId;
        try {
            authorId = UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException exception) {
            throw new UnauthorizedException("Invalid token");
        }

        threadService.deleteThread(id, authorId);

        return ResponseEntity.noContent().build();
    }

    /*1. The user sends a request for discussion threads in a course, optionally including search, filtering,
        sorting, and pagination parameters.
    2. The system verifies that the authenticated user has access to the course.
    3. The system validates the supplied query parameters.
    4. The system retrieves matching discussion threads.
    5. The system returns a paginated response containing the matching threads.
    * */
    @GetMapping("/threads")
    public ResponseEntity<Page<ThreadDto>> getAllThreads(
            @PageableDefault(size=10) Pageable pageable) {
        Page<ThreadDto> threads = threadService.getThreads(pageable);
        return ResponseEntity.ok(threads);
    }

}