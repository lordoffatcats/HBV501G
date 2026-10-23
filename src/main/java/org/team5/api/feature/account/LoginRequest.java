package org.team5.api.feature.account;

import jakarta.validation.constraints.NotEmpty;

public class LoginRequest {
    @NotEmpty
    public String username;

    @NotEmpty
    public String password;
}
