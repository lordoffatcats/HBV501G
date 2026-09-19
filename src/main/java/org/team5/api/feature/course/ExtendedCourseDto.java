package org.team5.api.feature.course;

import java.util.UUID;

public class ExtendedCourseDto {
    public final UUID id;
    public final String name;
    public final String joinCode;

    public ExtendedCourseDto(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.joinCode = course.getJoinCode();
    }
}
