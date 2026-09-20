package org.team5.api.feature.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.BadRequestException;
import org.team5.api.exceptions.ForbiddenException;
import org.team5.api.exceptions.UnauthorizedException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/courses")
    public ResponseEntity<ExtendedCourseDto> createCourse(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }

        String name = body.get("name");
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name is required");
        }

        ExtendedCourseDto result = new ExtendedCourseDto(courseService.createCourse(name));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/courses/{id}/join")
    public ResponseEntity<Void> joinCourse(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID userId = UUID.fromString(subject);
        String joinCode = body.get("joinCode");
        if (joinCode == null || joinCode.isBlank()) {
            throw new BadRequestException("Join code is required");
        }

        courseService.joinCourse(id, userId, joinCode);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/courses/{id}/leave")
    public ResponseEntity<Void> leaveCourse(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID userId = UUID.fromString(subject);
        courseService.leaveCourse(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/mine")
    public ResponseEntity<List<CourseDto>> getUserCourses(@AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null) {
            throw new UnauthorizedException("Invalid token");
        }

        UUID userId = UUID.fromString(subject);
        List<CourseDto> courses = courseService.getUserCourses(userId)
            .stream()
            .map(CourseDto::new)
            .toList();

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<ExtendedCourseDto>> getAllCourses(@AuthenticationPrincipal Jwt jwt) {
        if (!Boolean.TRUE.equals(jwt.getClaim("isAdmin"))) {
            throw new ForbiddenException("You do not have permission to perform this action");
        }

        List<ExtendedCourseDto> courses = courseService.getAllCourses()
            .stream()
            .map(ExtendedCourseDto::new)
            .toList();

        return ResponseEntity.ok(courses);
    }
}
