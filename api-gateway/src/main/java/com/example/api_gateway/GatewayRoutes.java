package com.example.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class GatewayRoutes {
//    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/users/**")
//                        .filters(f -> f.rewritePath("/(?<segment>/?.*)", "/api${segment}"))
//                        .filters(f -> f.rewritePath("/(?<segment>.*)", "/api/${segment}"))
                        .uri("lb://USER-SERVICE"))
                .route("product-service", r -> r
                        .path("/api/products/**")
                        .uri("lb://USER-SERVICE"))
                .route("eureka", r -> r
                        .path("/eureka", "/eureka/")
                        .filters(f -> f.rewritePath("/eureka", "/"))
                        .uri("http://localhost:8761"))
                .route("eureka", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
}
