package com.example.api_gateway.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class GatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 1, 1);
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("eureka", r -> r
                        .path("/eureka", "/eureka/")
                        .filters(f -> f.rewritePath("/eureka", "/"))
                        .uri("http://localhost:8761"))
                .route("eureka-static", r -> r
                        .path("/eureka/**")
                        .uri("http://localhost:8761"))

                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://USER-SERVICE"))
                .route("user-service-admin", r -> r
                        .path("/admin/users/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://USER-SERVICE"))
                .route("user-service-docs", r -> r
                        .path("/user/v3/api-docs")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://USER-SERVICE"))

                .route("product-service", r -> r
                        .path("/api/products/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://PRODUCT-SERVICE"))
                .route("product-service-admin", r -> r
                        .path("/admin/products/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://PRODUCT-SERVICE"))
                .route("product-service-docs", r -> r
                        .path("/product/v3/api-docs")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://PRODUCT-SERVICE"))

                .route("cart-service", r -> r
                        .path("/api/carts/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://CART-SERVICE"))
                .route("cart-service-docs", r -> r
                        .path("/cart/v3/api-docs")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://CART-SERVICE"))

                .route("order-service", r -> r
                        .path("/api/orders/**")
                        .filters(f -> f.tokenRelay())
//                                .requestRateLimiter(config -> config.
//                                setRateLimiter(redisRateLimiter())
//                                .setKeyResolver(userKeyResolver()))
                        .uri("lb://ORDER-SERVICE"))
                .route("order-service-admin", r -> r
                        .path("/admin/orders/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://ORDER-SERVICE"))
                .route("order-service-docs", r -> r
                        .path("/order/v3/api-docs")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ORDER-SERVICE"))

                .route("notification-service", r -> r
                        .path("/api/notifications/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://NOTIFICATION-SERVICE"))
                .route("notification-service-docs", r -> r
                        .path("/notification/v3/api-docs")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://NOTIFICATION-SERVICE"))

                .route("payment-service", r -> r
                        .path("/api/payments/**")
                        .filters(GatewayFilterSpec::tokenRelay)
                        .uri("lb://PAYMENT-SERVICE"))
                .route("payment-service-docs", r -> r
                        .path("/payment/v3/api-docs")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://PAYMENT-SERVICE"))
                .build();
    }
}
