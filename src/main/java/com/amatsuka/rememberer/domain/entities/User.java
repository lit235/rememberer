package com.amatsuka.rememberer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private @NonNull String login;

    @Column(nullable = false)
    private @NonNull String name;

    @Column(nullable = false)
    private @NonNull String passwordHash;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Date loginAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
