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
                .route("user-service", rt -> rt.path("/users/**")
                        .uri("http://localhost:3005/"))
                .route("post-service", rt -> rt.path("/posts/**")
                        .uri("http://localhost:3010/"))
                .route("comment-service", rt -> rt.path("/posts/**")
                        .uri("http://localhost:3015/"))
                .route("like-service", rt -> rt.path("/postsOrComments/**")
                        .uri("http://localhost:3020/"))
                .build();
    }
}
