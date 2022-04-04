package com.example.demo.filter;

import com.example.demo.Utility.JWTUtili;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JWTGatewayFilter implements GatewayFilter {

    @Autowired
    private JWTUtili jwtUtility;



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        List<String> authHeaders = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);
        if (authHeaders.isEmpty()) {
            return completeResponse(exchange, HttpStatus.UNAUTHORIZED);
        }

        final String authHeader = authHeaders.iterator().next();
        if (!authHeader.startsWith("Bearer ")) {
            return completeResponse(exchange, HttpStatus.BAD_REQUEST);
        }

        final String token = authHeader.substring(7);
        try {
           final String username = jwtUtility.getUsernameFromToken(token);
            if (!jwtUtility.validateToken(token)) {
                return completeResponse(exchange, HttpStatus.UNAUTHORIZED);
            }

            request.mutate()
                    .header("user", username)
                    .build();
            return chain.filter(exchange);
        } catch (MalformedJwtException | ExpiredJwtException e) {
            return completeResponse(exchange, HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> completeResponse(ServerWebExchange exchange, HttpStatus errorStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(errorStatus);
        return response.setComplete();
    }
}
