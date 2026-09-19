package org.team5.api.feature.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ExtendedCourseDto> createCourse(@RequestBody Map<String, String> body) {
        // TODO: Authorization
        String name = body.get("name");
        ExtendedCourseDto result = new ExtendedCourseDto(courseService.createCourse(name));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/courses/{id}/join")
    public ResponseEntity<Void> joinCourse(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        // TODO: Account connection
        String joinCode = body.get("joinCode");
        courseService.joinCourse(id, id, joinCode);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/courses/{id}/leave")
    public ResponseEntity<Void> leaveCourse(@PathVariable UUID id) {
        // TODO: Account connection
        courseService.leaveCourse(id, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/courses/mine")
    public ResponseEntity<List<CourseDto>> getCourses() {
        // TODO: Account connection
        UUID id = UUID.randomUUID();
        List<CourseDto> courses = courseService.getUserCourses(id)
            .stream()
            .map(CourseDto::new)
            .toList();

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        // TODO: Admin authentication
        List<CourseDto> courses = courseService.getAllCourses()
            .stream()
            .map(CourseDto::new)
            .toList();

        return ResponseEntity.ok(courses);
    }
}
