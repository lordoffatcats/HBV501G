package org.team5.api.feature.discussion;

import org.team5.api.feature.account.AccountDto;
import org.team5.api.feature.course.CourseDto;

import java.time.Instant;
import java.util.UUID;

public class ThreadDto {
    public final UUID id;
    public final String title;
    public final String content;
    public final AccountDto author;
    public final CourseDto course;
    public final Instant createdAt;

    public ThreadDto(Thread thread) {
        this.id = thread.getId();
        this.title = thread.getTitle();
        this.content = thread.getContent();
        this.author = new AccountDto(thread.getAuthor());
        this.course = new CourseDto(thread.getCourse());
        this.createdAt = thread.getCreatedAt();
    }
}
