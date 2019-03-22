package com.amatsuka.rememberer.configuration;

import com.amatsuka.rememberer.security.users.JwtUsersConfigurer;
import com.amatsuka.rememberer.security.users.JwtUsersTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Configuration
    @Order(1)
    @RequiredArgsConstructor
    public static class ApiClientsSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private final JwtUsersConfigurer jwtUsersConfigurer;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //@formatter:off
            http.antMatcher("/api/**")
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/**").hasAuthority("API_CLIENT")
                    .and()
                    .apply(jwtUsersConfigurer);
            //@formatter:on
        }
    }


    @Configuration
    @Order(2)
    @RequiredArgsConstructor
    public static class UsersSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private final JwtUsersConfigurer jwtUsersConfigurer;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //@formatter:off
            http
                    .antMatcher("/admin/**")
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/admin/auth/signin").permitAll()
                    .antMatchers("/admin/auth/signup").permitAll()
                    .antMatchers("/admin/**").hasAuthority("USER")
                    .and()
                    .apply(jwtUsersConfigurer);
            //@formatter:on
        }
    }
}