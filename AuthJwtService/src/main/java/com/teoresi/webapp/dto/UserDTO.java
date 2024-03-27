package com.teoresi.webapp.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserDTO {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private String token;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
