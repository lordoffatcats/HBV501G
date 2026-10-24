package org.team5.api.feature.discussion;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.feature.SubjectExtractor;

import java.util.List;
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
            @Valid @RequestBody CreateThreadRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID authorId = SubjectExtractor.GetSubject(jwt);
        Thread thread = threadService.createThread(
            courseId,
            authorId,
            request.title,
            request.content
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ThreadDto(thread));
    }

    @GetMapping("/threads/{id}")
    public ResponseEntity<ThreadDto> getThread(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        UUID viewerId = SubjectExtractor.GetSubject(jwt);
        Thread thread = threadService.getThread(id, viewerId);
        return ResponseEntity.ok(new ThreadDto(thread));
    }

    @PatchMapping("/threads/{id}")
    public ResponseEntity<ThreadDto> editThread(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateThreadRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        UUID authorId = SubjectExtractor.GetSubject(jwt);
        Thread thread = threadService.updateThread(
            id,
            authorId,
            request.title,
            request.content
        );

        return ResponseEntity.ok(new ThreadDto(thread));
    }

    @DeleteMapping("/threads/{id}")
    public ResponseEntity<Void> deleteThread(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        UUID authorId = SubjectExtractor.GetSubject(jwt);
        threadService.deleteThread(id, authorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/threads")
    public ResponseEntity<Page<ThreadDto>> getAllThreads(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) UUID courseId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal Jwt jwt) {

        /* Todo: Breytti þessu vegna þess að ég breytti token generationinu.
            þetta er algjört skítamix hjá mér til að laga ekki admin Jwt tjékkið.
            Veit samt ekki hvers vegna þetta if tjékk er gert.
            Hvers vegna að leyfa admin gera courseId=null ??????
            Nú veit ég ekki hvernig þetta pageable stuff virkar, en getThreads
            gerir hasCourseId(null) þá. Veit ekki hvort það errorar eða gerir eih gagnlegt.
         */
        List<String> roles = jwt.getClaimAsStringList("roles");
        boolean isAdmin = roles != null && roles.contains("ADMIN");
        if (courseId == null && !isAdmin) {
            throw new ForbiddenException("You are required to provide a course id");
        }

        Page<ThreadDto> threads = threadService.getThreads(pageable, query, courseId);
        return ResponseEntity.ok(threads);
    }

}