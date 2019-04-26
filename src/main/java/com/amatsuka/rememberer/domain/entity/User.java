package com.amatsuka.rememberer.domain.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

import static java.util.Arrays.asList;

@Entity
@Data
@EqualsAndHashCode(exclude = "apiClients")
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {

    @Column(nullable = false, unique = true, length = 20)
    @NonNull
    private String username;

    @Column
    private String name;

    @Column(nullable = false, length = 255)
    @NonNull
    private String passwordHash;

    @Column(nullable = false)
    @Builder.Default
    private Date createdAt = null;

    @Column
    @Builder.Default
    private Date loginAt = null;

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
    public String getUsername() {
        return username;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ApiClient> apiClients = new HashSet<>();
}
