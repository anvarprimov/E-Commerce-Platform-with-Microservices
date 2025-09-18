package com.ecommerce.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                    CorsConfigurationSource corsSource) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsSource))
                .authorizeHttpRequests(auth -> auth
                        // keep health open; lock everything else
                        .requestMatchers("/actuator/health").permitAll()
                        // If you want Swagger protected, do NOT permit these:
                        // .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthConverter()))
                );

        return http.build();
    }

    private JwtAuthenticationConverter keycloakJwtAuthConverter() {
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmClientRoleConverter());
        return converter;
    }

    @Bean
    @Primary
    public CorsConfigurationSource corsConfigurationSource() {
        return CorsSupport.permissive();
    }
}
