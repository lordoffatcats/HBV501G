package org.team5.api.feature.discussion;

import jakarta.validation.constraints.Size;

public class UpdateThreadRequest {
    @Size(max = 100)
    public String title;

    public String content;
}
