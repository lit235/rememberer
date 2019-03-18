package com.amatsuka.rememberer.sevices;

import com.amatsuka.rememberer.domain.repositories.ApiClientsRepository;
import com.amatsuka.rememberer.domain.repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ApiClientsDetailsService implements UserDetailsService {

    private ApiClientsRepository repository;

    public ApiClientsDetailsService(ApiClientsRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException {

        return this.repository.findByClientId(clientId)
                .orElseThrow(() -> new UsernameNotFoundException("Client id: " + clientId + " not found"));
    }
}
