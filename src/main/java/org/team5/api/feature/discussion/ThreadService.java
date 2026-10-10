package org.team5.api.feature.discussion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ThreadService {
    Thread createThread(UUID courseId, UUID authorId, String title, String content);

    Thread getThread(UUID threadId, UUID viewerId);

    Page<ThreadDto> getThreads(Pageable pageable);

    Thread updateThread(UUID threadId, UUID authorId, String title, String content);

    void deleteThread(UUID threadId, UUID authorId);
}