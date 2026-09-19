package org.team5.api.feature.course;

import jakarta.persistence.*;
import org.team5.api.feature.account.Account;

import java.util.List;
import java.util.UUID;

@Entity
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String joinCode;

    @ManyToMany
    @JoinTable(
        name = "course_members",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "members_id")
    )
    private List<Account> members;

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getJoinCode() {
        return this.joinCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getMembers() {
        return this.members;
    }

    public Course() {}

    public Course(String name, String joinCode) {
        this.name = name;
        this.joinCode = joinCode;
    }
}
