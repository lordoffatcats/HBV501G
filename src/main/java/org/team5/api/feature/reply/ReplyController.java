package org.team5.api.feature.reply;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.feature.SubjectExtractor;

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
            @CookieValue @RequestBody CreateReplyRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        UUID accountId = SubjectExtractor.GetSubject(jwt);
        Reply reply = replyService.createReply(
            threadId,
            accountId,
            request.content
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ReplyDto(reply));
    }

    @PatchMapping("/replies/{id}")
    public ResponseEntity<ReplyDto> editReply(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateReplyRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        UUID accountId = SubjectExtractor.GetSubject(jwt);
        Reply reply = replyService.updateReply(
            id,
            accountId,
            request.content
        );

        return ResponseEntity.ok(new ReplyDto(reply));
    }

    @DeleteMapping("/replies/{id}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        UUID accountId = SubjectExtractor.GetSubject(jwt);
        replyService.deleteReply(id, accountId);
        return ResponseEntity.noContent().build();
    }
}