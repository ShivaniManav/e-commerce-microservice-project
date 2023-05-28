package com.sm.gateway.filter;

import com.sm.core.exception.SMServiceException;
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
                if(exchange.getRequest().getCookies().containsKey(ACCESS_TOKEN)) {
                    throw new SMServiceException("missing authorization header");
                }
                String token = exchange.getRequest().getCookies().get(ACCESS_TOKEN).toString();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Cookie", ACCESS_TOKEN+"="+token);
                Boolean authStatus = restTemplate.exchange("http://iam-service/iam/auth/validate",
                        HttpMethod.GET, new HttpEntity<>(headers), Boolean.class).getBody();
                if(Boolean.FALSE.equals(authStatus)) {
                    throw new SMServiceException("Invalid token provided");
                }
            }
            exchange.getRequest().mutate().header("X-auth-user-id", "12").build();
            return chain.filter(exchange);
        });
    }

    public static class Config {}
}
