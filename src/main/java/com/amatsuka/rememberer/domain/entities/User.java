package com.amatsuka.rememberer.domain.entities;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Entity
@Data
@EqualsAndHashCode(exclude = "apiClients")
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {

    @Column(nullable = false, unique = true, length = 20)
    private @NonNull String username;

    @Column
    private String name;

    @Column(nullable = false, length = 255)
    private @NonNull String passwordHash;

    @Column(nullable = false)
    private Date createdAt;

    @Column
    private Date loginAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    public List<String> getRoles() {
        return asList("USER");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>() {{
            add((GrantedAuthority) () -> "USER");
        }};
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
