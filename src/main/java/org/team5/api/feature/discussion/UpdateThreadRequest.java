package org.team5.api.feature.discussion;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UpdateThreadRequest {
    @NotEmpty
    @Size(max = 100)
    public String title;

    @NotEmpty
    public String content;
}
