package org.team5.api.feature.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UpdateAccountRequest {
    @Email
    @NotEmpty
    public String email;
}