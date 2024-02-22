package com.teoresi.webapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class BasicAuthSecurityConfiguration {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(
                auth->{
                    auth.anyRequest().authenticated();
                });
        http.sessionManagement(
                session->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        //http.formLogin();
        http.httpBasic();
        http.csrf().disable();
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

/**
    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails user = User.withUsername("user")
                .password("{noop}password")
                .roles("USER").build();
        UserDetails admin = User.withUsername("admin")
                .password("{noop}password")
                .roles("ADMIN","USER").build();

        return new InMemoryUserDetailsManager(user,admin);
    }


 */
    @Bean
    public UserDetailsService userDetailsService(){

        User.UserBuilder users = User.builder();

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(users
                .username("user")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles("USER").build());

        manager.createUser(users
                .username("admin")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles("USER", "ADMIN").build());

        return manager;
    }


}
