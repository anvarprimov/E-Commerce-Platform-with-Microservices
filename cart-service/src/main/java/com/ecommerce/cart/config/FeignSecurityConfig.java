package com.ecommerce.cart.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class FeignSecurityConfig {
    @Bean
    public RequestInterceptor bearerTokenRelayInterceptor() {
        return template -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                template.header("Authorization", "Bearer " + jwt.getTokenValue());
            }
        };
    }
}
