package com.sm.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.core.dto.response.SMServiceExceptionResponse;
import com.sm.core.exception.SMServiceException;
import com.sm.gateway.dto.AuthenticationResponse;
import com.sm.gateway.dto.Authority;
import com.sm.gateway.validator.RouterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final String JWT_TOKEN = "jwt_token";

    @Autowired
    private RouterValidator validator;

    @Autowired
    private final WebClient.Builder webClient;

    @Autowired
    private ObjectMapper mapper;

    public AuthenticationFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if(validator.isSecured.test((ServerHttpRequest) exchange.getRequest())) {
                if (exchange.getRequest().getCookies().containsKey(JWT_TOKEN)) {
                    throw new SMServiceException("missing jwt token in cookies");
                }
                String token = exchange.getRequest().getCookies().get(JWT_TOKEN).toString();
                return webClient.build().post()
                        .uri("lb://iam-service/iam/auth/validate")
                        .header("Cookie", JWT_TOKEN + "=" + token)
                        .retrieve().bodyToMono(AuthenticationResponse.class)
                        .map(authResponse -> {
                            exchange.getRequest().mutate().header("username", authResponse.getUsername())
                                .build();
                            exchange.getRequest().mutate().header("authorities", authResponse.getAuthorities()
                                    .stream().map(Authority::getRole).collect(Collectors.joining(","))).build();
                            exchange.getRequest().mutate().header(JWT_TOKEN, authResponse.getToken()).build();
                            return exchange;
                        }).flatMap(chain::filter).onErrorResume(error -> {
                            HttpStatus errorCode = null;
                            if(error instanceof WebClientResponseException) {
                                WebClientResponseException webClientResponseException = (WebClientResponseException) error;
                                errorCode = webClientResponseException.getStatusCode();
                            } else {
                                errorCode = HttpStatus.BAD_GATEWAY;
                            }
                            return onError(exchange, errorCode);
                        });
            }
            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        try {
            response.getHeaders().add("Content-Type", "application/json");
            SMServiceExceptionResponse errorResponse = new SMServiceExceptionResponse("JWT Authentication Failed!",
                    httpStatus.value(), httpStatus.value());
            byte[] byteData = mapper.writeValueAsBytes(errorResponse);
            return response.writeWith(Mono.just(byteData).map(dataBufferFactory::wrap));
        } catch (JsonProcessingException e) {
            //
        }
        return response.setComplete();
    }

    public static class Config {}
}
