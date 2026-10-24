package org.team5.api.feature.course;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.feature.SubjectExtractor;

import java.util.List;
import java.util.UUID;

@RestController
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExtendedCourseDto> createCourse(
            @Valid @RequestBody CreateCourseRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        /* TODO: I think @PreAuthorize("hasRole('ADMIN')") makes this check redundant now
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }
        */

        ExtendedCourseDto result = new ExtendedCourseDto(courseService.createCourse(request.name));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/courses/{id}/join")
    public ResponseEntity<Void> joinCourse(
            @PathVariable UUID id,
            @Valid @RequestBody JoinCourseRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = SubjectExtractor.GetSubject(jwt);
        courseService.joinCourse(id, userId, request.joinCode);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/courses/{id}/leave")
    public ResponseEntity<Void> leaveCourse(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = SubjectExtractor.GetSubject(jwt);
        courseService.leaveCourse(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/mine")
    public ResponseEntity<List<CourseDto>> getUserCourses(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = SubjectExtractor.GetSubject(jwt);
        List<CourseDto> courses = courseService.getUserCourses(userId)
            .stream()
            .map(CourseDto::new)
            .toList();

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ExtendedCourseDto>> getAllCourses(@AuthenticationPrincipal Jwt jwt) {

        /* TODO: I think @PreAuthorize("hasRole('ADMIN')") makes this check redundant now
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }
        */

        List<ExtendedCourseDto> courses = courseService.getAllCourses()
            .stream()
            .map(ExtendedCourseDto::new)
            .toList();

        return ResponseEntity.ok(courses);
    }
}
