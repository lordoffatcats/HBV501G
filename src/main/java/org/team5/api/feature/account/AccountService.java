package org.team5.api.feature.account;

import java.util.UUID;

public interface AccountService {
    Account createAccount(String email, String username, String password, boolean isAdmin);
    String authenticate(String username, String password);
    Account getAccount(UUID id);
    Account updateAccount(UUID id, String email);
    void deleteAccount(UUID id);
    Account updateProfilePicture(UUID id, byte[] profilePicture);
}
