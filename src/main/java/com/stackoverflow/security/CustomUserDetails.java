package com.stackoverflow.security;

import com.stackoverflow.model.User;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {
    private final Long id;

    public CustomUserDetails(User user) {
        super(user.getEmail(), user.getPassword(),
                user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }
}
