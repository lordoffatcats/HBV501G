package org.team5.api.feature.discussion;

import java.time.Instant;
import java.util.UUID;

public class DiscussionThreadDto {
    public final UUID id;
    public final String title;
    public final String content;
    public final UUID authorId;
    public final String authorUsername;
    public final UUID courseId;
    public final Instant createdAt;

    public DiscussionThreadDto(DiscussionThread thread) {
        this.id = thread.getId();
        this.title = thread.getTitle();
        this.content = thread.getContent();
        this.authorId = thread.getAuthor().getId();
        this.authorUsername = thread.getAuthor().getUsername();
        this.courseId = thread.getCourse().getId();
        this.createdAt = thread.getCreatedAt();
    }
}
