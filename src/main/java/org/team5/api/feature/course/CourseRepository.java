package org.team5.api.feature.course;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends Repository<Course, UUID> {
    Course save(Course course);
    Optional<Course> findById(UUID id);
    // TODO: List<Course> findByMemberId(UUID accountId);
    List<Course> findAll();
    // TODO: boolean isUserInCourse(UUID courseId, UUID userId);
}
