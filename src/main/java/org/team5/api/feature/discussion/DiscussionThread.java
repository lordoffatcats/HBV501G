package org.team5.api.feature.discussion;

import jakarta.persistence.*;
import org.team5.api.feature.account.Account;
import org.team5.api.feature.course.Course;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "discussion_threads")
public class DiscussionThread {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Account author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public DiscussionThread() {}

    public DiscussionThread(String title, String content, Account author, Course course) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.course = course;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Account getAuthor() {
        return author;
    }

    public Course getCourse() {
        return course;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
