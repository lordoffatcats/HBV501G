package org.team5.api.feature.account;

import jakarta.validation.constraints.NotEmpty;

public class UploadProfilePictureRequest {
    @NotEmpty
    public String profilePicture;
}
