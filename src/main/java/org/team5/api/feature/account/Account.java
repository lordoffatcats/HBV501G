package org.team5.api.feature.account;

import jakarta.persistence.*;
import org.team5.api.feature.course.Course;

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

    @ManyToMany(mappedBy = "members")
    private List<Course> courses;

    public UUID getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public byte[] getProfilePicture() {
        return this.profilePicture;
    }

    public List<Course> getCourses() {
        return this.courses;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Account() {}

    public Account(String email, String username, String password, boolean isAdmin) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
