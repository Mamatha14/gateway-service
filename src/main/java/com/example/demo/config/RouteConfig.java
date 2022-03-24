package com.example.demo.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("user-service", rt -> rt.path("/api/v1/users/**")
                        .uri("http://localhost:3005/"))
                .route("comment-service", rt -> rt.path("/api/v1/posts/*/comments/**")
                        .uri("http://localhost:3015/"))
                .route("post-service", rt -> rt.path("/api/v1/posts/**")
                        .uri("http://localhost:3010/"))
                .route("like-service", rt -> rt.path("/api/v1/postsOrComments/*/likes/**")
                        .uri("http://localhost:3020/"))
                .build();
    }
}
