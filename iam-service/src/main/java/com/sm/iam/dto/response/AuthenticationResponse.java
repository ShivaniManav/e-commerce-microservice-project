package com.sm.iam.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@ToString
public class AuthenticationResponse {

    private String status;

    private String username;

    private String token;

    private List<GrantedAuthority> authorities;

    private boolean isAuthenticated;
}
