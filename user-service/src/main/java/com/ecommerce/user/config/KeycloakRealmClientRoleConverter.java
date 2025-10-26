package com.ecommerce.user.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakRealmClientRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    String clientId = "gateway";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return List.of();
        }

        Object clientSection = resourceAccess.get(clientId);
        if (!(clientSection instanceof Map<?, ?> m)) {
            return List.of();
        }

        Object rolesObj = m.get("roles");
        if (!(rolesObj instanceof Collection<?> rolesRaw)) {
            return List.of();
        }

        return rolesRaw.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.toUpperCase()))
                .collect(Collectors.toList());
    }
}
