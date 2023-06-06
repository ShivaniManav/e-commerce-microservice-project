package com.sm.gateway.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class AuthenticationResponse {

    private String status;

    private String username;

    private String token;

    private List<Authority> authorities;

    private boolean isAuthenticated;
}
