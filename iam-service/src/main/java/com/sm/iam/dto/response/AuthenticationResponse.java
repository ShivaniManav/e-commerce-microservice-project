package com.sm.iam.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Builder
@Getter
@ToString
public class AuthenticationResponse {

    private String status;

    private String username;

    private String token;

    private Set<GrantedAuthority> authorities;

    private boolean isAuthenticated;
}
