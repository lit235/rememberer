package com.amatsuka.rememberer.service;

import com.amatsuka.rememberer.domain.entity.ApiClient;
import com.amatsuka.rememberer.domain.entity.User;
import com.amatsuka.rememberer.repository.ApiClientsRepository;
import com.amatsuka.rememberer.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UsersRepository usersRepository;

    private ApiClientsRepository apiClientsRepository;

    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository, ApiClientsRepository apiClientsRepository) {
        this.usersRepository = usersRepository;
        this.apiClientsRepository = apiClientsRepository;
    }


    //TODO перевести в geatway
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = this.usersRepository.findByUsername(username);

        if (user.isPresent()) {
            return user.get();
        }

        Optional<ApiClient> apiClient = this.apiClientsRepository.findByClientId(username);

        if (apiClient.isPresent()) {
            return apiClient.get();
        }

        throw new UsernameNotFoundException("Username: " + username + " not found");

        /*return this.usersRepository.findByUsername(username)
                .orElseGet(() -> this.apiClientsRepository.findByClientId(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found")));*/
    }
}
