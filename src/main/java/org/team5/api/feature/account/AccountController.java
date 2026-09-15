package org.team5.api.feature.account;

import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AccountController {
    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(Account account) {return null;};

    public String signIn(String username, String password) {return null;}

    public Account editAccount(UUID id, Account account) {return null;}

    public void deleteAccount(UUID id) {return;}

    public Account createAdminAccount(Account account) {return null;}

    public Account editAccountAsAdmin(UUID id, Account account) {return null;}

    public Account getAccountAsAdmin(UUID id) {return null;}

    public Account uploadProfilePicture(UUID id, byte[] profilePicture) {return null;}
}
