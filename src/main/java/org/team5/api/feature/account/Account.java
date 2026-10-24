package org.team5.api.feature.account;

import jakarta.persistence.*;
import org.team5.api.feature.course.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Account {
    /* TODO: Role er enum, en Jwt stuffið styður í raun mörg roles per Account.
     *  Veit bara ekki hvernig er best að útfæra þetta á þessu stigi.
    */
    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    }

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String username;

    private String password;

    @ManyToOne
    private Role role = Role.ROLE_USER; // Default to user role TODO: Veit ekki hvort það make-ar sens

    private byte[] profilePicture;

    @ManyToMany(mappedBy = "members")
    private List<Course> courses = new ArrayList<>();

    public UUID getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Account() {}

    public Account(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
