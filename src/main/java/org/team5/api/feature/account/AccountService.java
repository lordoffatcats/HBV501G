package org.team5.api.feature.account;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

public interface AccountService extends UserDetailsService {
    Account createAccount(String email, String username, String password, boolean isAdmin);
    Account getAccount(UUID id);
    Account updateAccount(UUID id, String email);
    void deleteAccount(UUID id);
    Account updateProfilePicture(UUID id, byte[] profilePicture);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
