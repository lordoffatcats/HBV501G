package org.team5.api.feature.discussion;

import java.util.UUID;

public interface DiscussionThreadService {
    DiscussionThread createThread(UUID courseId, UUID authorId, String title, String content);
}
