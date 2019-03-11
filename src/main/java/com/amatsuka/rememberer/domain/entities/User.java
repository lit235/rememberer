package com.amatsuka.rememberer.domain.entities;

import io.swagger.annotations.Api;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "apiClients")
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity  {

    @Column(nullable = false, unique = true, length = 20)
    private @NonNull String username;

    @Column
    private @NonNull String name;

    @Column(nullable = false)
    private @NonNull String passwordHash;

    @Column(nullable = false)
    private Date createdAt;

    @Column
    private Date loginAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

}
