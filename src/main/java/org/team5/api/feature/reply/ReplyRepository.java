package org.team5.api.feature.reply;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.team5.api.feature.account.Account;

import java.util.Optional;
import java.util.UUID;

public interface ReplyRepository extends Repository<Reply, UUID> {
    Reply save(Reply reply);
    Optional<Reply> findById(UUID id);
    void deleteById(UUID id);

    @Modifying
    @Query("UPDATE Reply r SET r.author = :newAuthor WHERE r.author.id = :authorId")
    void reassignAuthor(@Param("authorId") UUID authorId, @Param("newAuthor") Account newAuthor);
}