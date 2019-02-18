package com.amatsuka.rememberer.entities;

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
public class Record implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private @NonNull String text;

    @Column(nullable = false, unique = true, length = 20)
    private @NonNull String code;

    @Column(nullable = false)
    private Date createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
