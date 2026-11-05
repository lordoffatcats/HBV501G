package org.team5.api.feature.discussion;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.team5.api.feature.account.Account;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ThreadRepository extends PagingAndSortingRepository<Thread, UUID>, JpaSpecificationExecutor<Thread> {
    Thread save(Thread thread);

    Optional<Thread> findById(UUID id);

    void deleteById(UUID id);

    @Modifying
    @Query("UPDATE Thread t SET t.author = :newAuthor WHERE t.author.id = :authorId")
    void reassignAuthor(@Param("authorId") UUID authorId, @Param("newAuthor") Account newAuthor);
}