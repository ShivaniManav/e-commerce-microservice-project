package com.sm.gateway.filter;

import com.sm.core.exception.SMServiceException;
import com.sm.gateway.dto.AuthenticationResponse;
import com.sm.gateway.dto.Authority;
import com.sm.gateway.validator.RouterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@RefreshScope
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final String ACCESS_TOKEN = "access_token";

    @Autowired
    private RouterValidator validator;

    @Autowired
    private RestTemplate restTemplate;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(validator.isSecured.test((ServerHttpRequest) exchange.getRequest())) {
                if (exchange.getRequest().getCookies().containsKey(ACCESS_TOKEN)) {
                    throw new SMServiceException("missing access token in cookies");
                }
                String token = exchange.getRequest().getCookies().get(ACCESS_TOKEN).toString();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cookie", ACCESS_TOKEN + "=" + token);
                AuthenticationResponse authResponse = restTemplate.exchange("http://iam-service/iam/auth/validate",
                        HttpMethod.GET, new HttpEntity<>(headers), AuthenticationResponse.class).getBody();
                assert authResponse != null;
                if (!authResponse.isAuthenticated()) {
                    throw new SMServiceException("Invalid token provided");
                }
                exchange.getRequest().mutate().header("username", authResponse.getUsername())
                        .build();
                exchange.getRequest().mutate().header("authorities", authResponse.getAuthorities().stream()
                        .map(Authority::getRole).collect(Collectors.joining(","))).build();
                exchange.getRequest().mutate().header("access_token", authResponse.getToken()).build();
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {}
}
