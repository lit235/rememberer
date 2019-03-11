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
@Table(name = "api_clients")
public class ApiClient extends AbstractEntity  {

    @Column(nullable = false, unique = true, length = 20)
    private @NonNull String name;

    @Column(nullable = false, unique = true, length = 40)
    private @NonNull String clientId;

    @Column(nullable = false, unique = true, length = 40)
    private @NonNull String clientSecret;

    @Column(nullable = false)
    private Date createdAt;

    @Column
    private Date loginAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
