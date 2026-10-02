package org.team5.api.feature.reply;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface ReplyRepository extends Repository<Reply, UUID> {
    Reply save(Reply reply);
    Optional<Reply> findById(UUID id);
    void deleteById(UUID id);
}