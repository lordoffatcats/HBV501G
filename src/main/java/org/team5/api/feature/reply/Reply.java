package org.team5.api.feature.reply;

import jakarta.persistence.*;
import org.team5.api.feature.account.Account;
import org.team5.api.feature.discussion.Thread;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "replies")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Account author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thread_id", nullable = false)
    private Thread thread;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public Reply() {}

    public Reply(String content, Account author, Thread thread) {
        this.content = content;
        this.author = author;
        this.thread = thread;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Account getAuthor() {
        return author;
    }

    public Thread getThread() {
        return thread;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(Account author) {
        this.author = author;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}