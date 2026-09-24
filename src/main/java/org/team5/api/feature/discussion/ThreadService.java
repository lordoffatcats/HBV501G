package org.team5.api.feature.discussion;

import java.util.UUID;

public interface ThreadService {
    Thread createThread(UUID courseId, UUID authorId, String title, String content);
}
