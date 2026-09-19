package org.team5.api.feature.account;

import java.util.UUID;

public class AccountDto {
    private UUID id;
    private String username;
    private byte[] profilePicture;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.profilePicture = account.getProfilePicture();
    }
}
