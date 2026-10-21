package org.team5.api.feature.account;

import java.util.Base64;
import java.util.UUID;

public interface AccountService {
    Account createAccount(String email, String username, String password, boolean isAdmin);
    String authenticate(String username, String password);
    Account getAccount(UUID id);
    Account updateAccount(UUID id, UpdateAccountRequest request);
    void deleteAccount(UUID id);
    Account updateProfilePicture(UUID id, Base64 profilePicture);
}
