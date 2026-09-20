package org.team5.api.feature.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    public Account save(Account account);

    public Optional<Account> findById(UUID id);

    public Optional<Account> findByUsername(String username);

    void delete(Account account);
}
