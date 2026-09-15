package org.team5.api.feature.account;

import jakarta.persistence.*;
import org.team5.api.feature.reply.Reply;
import org.team5.api.feature.thread.Thread;

import java.util.List;
import java.util.UUID;

@Entity
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String password;

    private boolean isAdmin;

    private byte[] profilePicture;

    @OneToMany
    private List<Thread> threads;

    @OneToMany
    private List<Reply> replies;

    public Account() {}
}
