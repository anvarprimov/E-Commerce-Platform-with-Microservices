package com.ecommerce.product.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakRealmClientRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> roles = new ArrayList<>();

        // realm roles
//        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
//        if (realmAccess != null) {
//            Object realmRoles = realmAccess.get("roles");
//            if (realmRoles instanceof Collection<?> rr) {
//                rr.forEach(r -> roles.add(String.valueOf(r)));
//            }
//        }

        // client roles: resource_access.{client}.roles
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.values().forEach(val -> {
                if (val instanceof Map<?, ?> m) {
                    Object clientRoles = m.get("roles");
                    if (clientRoles instanceof Collection<?> cr) {
                        cr.forEach(r -> roles.add(String.valueOf(r)));
                    }
                }
            });
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());
    }
}