package org.team5.api.feature.discussion;

import java.util.UUID;

public interface ThreadService {
    Thread createThread(UUID courseId, UUID authorId, String title, String content);

    Thread getThread(UUID threadId, UUID viewerId);

    Thread updateThread(UUID threadId, UUID authorId, String title, String content);

    void deleteThread(UUID threadId, UUID authorId);
}