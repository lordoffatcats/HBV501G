package org.team5.api.feature.reply;

import jakarta.validation.constraints.NotEmpty;

public class CreateReplyRequest {
    @NotEmpty
    public String content;
}
