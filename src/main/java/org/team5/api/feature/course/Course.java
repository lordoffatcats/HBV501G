package org.team5.api.feature.course;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Course {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String joinCode;

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

    public Course() {}

    public Course(String name, String joinCode) {
        this.name = name;
        this.joinCode = joinCode;
    }
}
