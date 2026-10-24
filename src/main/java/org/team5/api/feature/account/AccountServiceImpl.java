package org.team5.api.feature.account;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team5.api.exceptions.ConflictException;
import org.team5.api.exceptions.NotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl  implements AccountService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole().toString())
                .build();
    }

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(String email, String username, String password, boolean isAdmin) {
        Optional<Account> existingAccount = accountRepository.findByUsername(username);
        if (existingAccount.isPresent()) {
            throw new ConflictException("Username already exists");
        }
        String hashedPassword = passwordEncoder.encode(password);
        Account account = new Account(email, username, hashedPassword);
        if (isAdmin) {account.setRole(Account.Role.ROLE_ADMIN);}
        return accountRepository.save(account);
    }

    public Account getAccount(UUID id) {
        // TODO:
        return null;
    }

    public Account updateAccount(UUID id, String email) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow( () -> new NotFoundException("Account no found with id: " + id));

        if (email != null) {
            existingAccount.setEmail(email);
        }

        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(UUID id) {
        // TODO:
    }

    public Account updateProfilePicture(UUID id, byte[] profilePicture) {
        // TODO:
        return null;
    }
}
