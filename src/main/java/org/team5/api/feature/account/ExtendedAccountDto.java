package org.team5.api.feature.account;

import java.util.UUID;

public class ExtendedAccountDto {
    public final UUID id;
    public final String username;
    public final String email;
    public final byte[] profilePicture;

    public ExtendedAccountDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
        this.profilePicture = account.getProfilePicture();
    }
}
