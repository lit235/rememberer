package com.amatsuka.rememberer.domain.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

import static java.util.Arrays.asList;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "api_clients")
public class ApiClient extends AbstractEntity implements UserDetails {

    @Column(nullable = false, unique = true, length = 20)
    @NonNull
    private String name;

    @Column(nullable = false, length = 40)
    @NonNull
    private String clientId;

    @Column(nullable = false)
    private Date createdAt;

    @Column
    private Date loginAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public List<String> getRoles() {
        return asList("API_CLIENT");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>() {{
            add((GrantedAuthority) () -> "API_CLIENT");
        }};
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return clientId;
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
