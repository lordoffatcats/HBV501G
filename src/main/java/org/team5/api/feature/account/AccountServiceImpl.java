package org.team5.api.feature.account;

import org.springframework.stereotype.Service;
import org.team5.api.exceptions.BadRequestException;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl  implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(String email, String username, String password, boolean isAdmin) {
        Optional<Account> existingAccount = accountRepository.findByUsername(username);
        if (existingAccount.isPresent()) {
            throw new BadRequestException("Username already exists");
        }
        return accountRepository.save(new Account(email, username, password, isAdmin));
    };

    public String authenticate(String username, String password) {
        // TODO:
        return null;
    };

    public Account getAccount(UUID id) {
        // TODO:
        return null;
    };

    public Account updateAccount(UUID id, String email) {
        // TODO:
        return null;
    };

    public void deleteAccount(UUID id) {
        // TODO:
    };

    public Account updateProfilePicture(UUID id, byte[] profilePicture) {
        // TODO:
        return null;
    };
}
