package org.team5.api.feature.reply;

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
public class ReplyController {
    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/threads/{threadId}/replies")
    public ResponseEntity<ReplyDto> createReply(
            @PathVariable UUID threadId,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID accountId = UUID.fromString(subject);
        String content = body.get("content");

        if (content == null || content.isBlank()) {
            throw new BadRequestException("Content is required");
        }

        Reply reply = replyService.createReply(
                threadId,
                accountId,
                content
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ReplyDto(reply));
    }

    @PatchMapping("/replies/{id}")
    public ResponseEntity<ReplyDto> editReply(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt) {

        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID accountId = UUID.fromString(subject);
        String content = body.get("content");

        if (content == null || content.isBlank()) {
            throw new BadRequestException("Content is required");
        }

        Reply reply = replyService.updateReply(
                id,
                accountId,
                content
        );

        return ResponseEntity.ok(new ReplyDto(reply));
    }

    @DeleteMapping("/replies/{id}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {

        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID accountId = UUID.fromString(subject);

        replyService.deleteReply(
                id,
                accountId
        );

        return ResponseEntity.noContent().build();
    }
}