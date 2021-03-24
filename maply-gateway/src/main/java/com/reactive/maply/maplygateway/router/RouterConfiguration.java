package com.reactive.maply.maplygateway.router;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouterConfiguration {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(ps -> ps
                        .path("/api/position/**")
                        .or()
                        .path("/sse/position/**")
                        .uri("http://localhost:8081"))
                .route(ps -> ps
                        .path("/ws/position/**")
                        .uri("ws://localhost:8081"))
                .route(ps -> ps
                        .path("/api/users/**")
                        .uri("http://localhost:8082"))
                .build();
    }
}

