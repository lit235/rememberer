package com.amatsuka.rememberer.security.users;

import com.amatsuka.rememberer.sevices.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtUsersConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService);
        JwtUsersTokenFilter customFilter = new JwtUsersTokenFilter(new JwtUsersTokenProvider(userDetailsService));
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
