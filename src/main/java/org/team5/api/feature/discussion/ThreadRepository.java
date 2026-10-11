package org.team5.api.feature.discussion;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ThreadRepository extends PagingAndSortingRepository<Thread, UUID>, JpaSpecificationExecutor<Thread> {
    Thread save(Thread thread);

    Optional<Thread> findById(UUID id);

    void deleteById(UUID id);
}