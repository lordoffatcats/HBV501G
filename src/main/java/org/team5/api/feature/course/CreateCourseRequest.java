package org.team5.api.feature.course;

import jakarta.validation.constraints.NotEmpty;

public class CreateCourseRequest {
    @NotEmpty
    public String name;
}
