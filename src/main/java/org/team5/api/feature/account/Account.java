package org.team5.api.feature.account;

import jakarta.persistence.*;
import org.team5.api.feature.course.Course;
//import org.team5.api.feature.reply.Reply;
//import org.team5.api.feature.thread.Thread;

import java.util.List;
import java.util.UUID;

@Entity
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String username;

    private String password;

    private boolean isAdmin;

    private byte[] profilePicture;

    @OneToMany(mappedBy = "members")
    private List<Course> courses;

    //@OneToMany
    //private List<Reply> replies;

    //@OneToMany
    //private List<Thread> threads;

    public UUID getId() {
        return id;
    }

    public String getEmail() { return this.email; }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() { return this.isAdmin; }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public List<Course> getCourses() { return this.courses; }

    public void setEmail(String email) { this.email = email; }

    public Account() {}

    public Account(String email, String username, String password, boolean isAdmin) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
