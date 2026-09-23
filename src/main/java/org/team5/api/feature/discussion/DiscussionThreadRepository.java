package org.team5.api.feature.discussion;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface DiscussionThreadRepository extends Repository<DiscussionThread, UUID> {
    DiscussionThread save(DiscussionThread thread);
    Optional<DiscussionThread> findById(UUID id);
}
