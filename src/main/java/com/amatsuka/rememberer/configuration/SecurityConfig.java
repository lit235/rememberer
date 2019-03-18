package com.amatsuka.rememberer.configuration;

import com.amatsuka.rememberer.security.apiclients.JwtApiClientsConfigurer;
import com.amatsuka.rememberer.security.users.JwtUsersConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }


    @Configuration
    @Order(1)
    public static class ApiClientsSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //@formatter:off
            http.antMatcher("/api/**")
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/records/**").authenticated()
                    .and()
                    .apply(new JwtApiClientsConfigurer());
            //@formatter:on
        }
    }


    @Configuration
    @Order(2)
    public static class UsersSecurityConfig extends WebSecurityConfigurerAdapter {

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
                    .antMatchers("/admin/**").authenticated()
                    .antMatchers("/admin/auth/signin").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .apply(new JwtUsersConfigurer());
            //@formatter:on
        }
    }
}