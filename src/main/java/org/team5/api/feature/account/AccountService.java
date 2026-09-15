package org.team5.api.feature.account;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface AccountService {
    public Account createAccount(Account account);

    public String authenticate(String username, String password);

    public Account getAccount(UUID id);

    public Account updateAccount(UUID id, Account account);

    public void deleteAccount(UUID id);

    public Account updateProfilePicture(UUID id, byte[] profilePicture);
}
