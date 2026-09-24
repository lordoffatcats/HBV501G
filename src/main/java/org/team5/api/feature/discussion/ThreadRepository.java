package org.team5.api.feature.discussion;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface ThreadRepository extends Repository<Thread, UUID> {
    Thread save(Thread thread);
    Optional<Thread> findById(UUID id);
}
