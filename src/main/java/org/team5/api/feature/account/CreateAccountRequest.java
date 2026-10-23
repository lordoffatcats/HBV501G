package org.team5.api.feature.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateAccountRequest {
    @Email
    @NotBlank
    public String email;

    @NotBlank
    public String username;

    @NotBlank
    public String password;
}