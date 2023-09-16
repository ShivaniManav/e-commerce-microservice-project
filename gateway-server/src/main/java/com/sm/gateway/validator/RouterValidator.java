package com.sm.gateway.validator;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final List<String> openApiEndpoints = Arrays.asList(
            "/iam/login",
            "/iam/auth/register");

    public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints
            .stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
