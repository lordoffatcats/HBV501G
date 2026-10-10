package org.team5.api.feature.discussion;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ThreadRepository extends PagingAndSortingRepository<Thread, UUID> {
    Thread save(Thread thread);

    Optional<Thread> findById(UUID id);

    void deleteById(UUID id);
}