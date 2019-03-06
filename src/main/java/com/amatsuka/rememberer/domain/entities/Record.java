package com.amatsuka.rememberer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Record extends AbstractEntity implements Serializable {

    @Column(nullable = false)
    private @NonNull String text;

    @Column(nullable = false, unique = true, length = 40)
    private @NonNull String code;

    @Column(length = 32)
    private String passwordHash;

    @Column(nullable = false)
    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
