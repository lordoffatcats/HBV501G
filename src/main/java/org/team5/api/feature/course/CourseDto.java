package org.team5.api.feature.course;

import java.util.UUID;

public class CourseDto {
    public final UUID id;
    public final String name;

    public CourseDto(Course course) {
        this.id = course.getId();
        this.name = course.getName();
    }
}
