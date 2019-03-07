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
public class User extends AbstractEntity  {

    @Column(nullable = false, unique = true)
    private @NonNull String username;

    @Column
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
