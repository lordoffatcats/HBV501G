package org.team5.api.feature.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team5.api.exceptions.BadRequestException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class CourseController {
    private final CourseService courseService;
    @Deprecated private final UUID userId = UUID.fromString("bb277ee4-eb29-430f-bff3-0df0ed8d581b");

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/courses")
    public ResponseEntity<ExtendedCourseDto> createCourse(@RequestBody Map<String, String> body) {
        // TODO: Authorization
        String name = body.get("name");
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name is required");
        }

        ExtendedCourseDto result = new ExtendedCourseDto(courseService.createCourse(name));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/courses/{id}/join")
    public ResponseEntity<Void> joinCourse(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        // TODO: Account connection
        String joinCode = body.get("joinCode");
        if (joinCode == null || joinCode.isBlank()) {
            throw new BadRequestException("Join code is required");
        }

        courseService.joinCourse(id, userId, joinCode);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/courses/{id}/leave")
    public ResponseEntity<Void> leaveCourse(@PathVariable UUID id) {
        // TODO: Account connection
        courseService.leaveCourse(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/mine")
    public ResponseEntity<List<CourseDto>> getUserCourses() {
        // TODO: Account connection
        List<CourseDto> courses = courseService.getUserCourses(userId)
            .stream()
            .map(CourseDto::new)
            .toList();

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<ExtendedCourseDto>> getAllCourses() {
        // TODO: Admin authentication
        List<ExtendedCourseDto> courses = courseService.getAllCourses()
            .stream()
            .map(ExtendedCourseDto::new)
            .toList();

        return ResponseEntity.ok(courses);
    }
}
