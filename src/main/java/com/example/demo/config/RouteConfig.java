package com.example.demo.config;

import com.example.demo.filter.JWTGatewayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Autowired
    private JWTGatewayFilter jwtGatewayFilter;
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("authentication-authorization-service", rt -> rt.path("/api/v1/auth/**")
                        .uri("http://localhost:3000/"))
                .route("user-service", rt -> rt.path("/api/v1/users/**")
                       .filters((f -> f.filter(jwtGatewayFilter)))
                        .uri("http://localhost:3005/"))
                .route("comment-service", rt -> rt.path("/api/v1/posts/*/comments/**")
                        .filters((f -> f.filter(jwtGatewayFilter)))
                        .uri("http://localhost:3015/"))
                .route("post-service", rt -> rt.path("/api/v1/posts/**")
                        .filters((f -> f.filter(jwtGatewayFilter)))
                        .uri("http://localhost:3010/"))
                .route("like-service", rt -> rt.path("/api/v1/postsOrComments/*/likes/**")
                        .filters((f -> f.filter(jwtGatewayFilter)))
                        .uri("http://localhost:3020/"))
                .build();
    }
}
