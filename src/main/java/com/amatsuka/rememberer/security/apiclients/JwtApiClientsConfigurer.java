package com.amatsuka.rememberer.security.apiclients;

import com.amatsuka.rememberer.security.users.JwtUsersTokenFilter;
import com.amatsuka.rememberer.sevices.ApiClientsDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtApiClientsConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private ApiClientsDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtApiClientsTokenFilter customFilter = new JwtApiClientsTokenFilter(new JwtApiClientsTokenProvider(userDetailsService));
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
