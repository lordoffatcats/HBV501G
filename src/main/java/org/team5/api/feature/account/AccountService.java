package org.team5.api.feature.account;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AccountService {
    public Account createAccount(String email, String username, String password, boolean isAdmin);

    public String authenticate(String username, String password);

    public Account getAccount(UUID id);

    public Account updateAccount(UUID id, String email);

    public void deleteAccount(UUID id);

    public Account updateProfilePicture(UUID id, byte[] profilePicture);
}
