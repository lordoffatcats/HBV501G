package org.team5.api.feature.course;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    Course createCourse(String name);
    void joinCourse(UUID courseId, UUID accountId, String joinCode);
    void leaveCourse(UUID courseId, UUID accountId);
    List<Course> getUserCourses(UUID accountId);
    List<Course> getAllCourses();
}
