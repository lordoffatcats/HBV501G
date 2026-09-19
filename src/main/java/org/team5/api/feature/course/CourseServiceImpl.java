package org.team5.api.feature.course;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {
    // TODO: Account repo
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course createCourse(String name) {
        // TODO: Generate a join code
        return courseRepository.save(new Course(name, name));
    }

    @Override
    public void joinCourse(UUID courseId, UUID accountId, String joinCode) {
        // TODO: Account dependency
    }

    @Override
    public void leaveCourse(UUID courseId, UUID accountId) {
        // TODO: Account dependency
    }

    @Override
    public List<Course> getUserCourses(UUID accountId) {
        // TODO: Account dependency
        return List.of();
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
