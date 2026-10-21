package org.team5.api.feature.account;

import java.util.Base64;
import java.util.UUID;

public class AccountDto {
    public final UUID id;
    public final String username;
    public final Base64 profilePicture;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.profilePicture = account.getProfilePicture();
    }
}
