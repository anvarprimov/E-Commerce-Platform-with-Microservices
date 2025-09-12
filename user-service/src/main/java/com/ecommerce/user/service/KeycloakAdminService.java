package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {
    private final RestTemplate restTemplate;
    @Value("${keycloak.admin.username}")
    private  String username;
    @Value("${keycloak.admin.password}")
    private  String password;
    @Value("${keycloak.admin.serverUrl}")
    private  String serverUrl;
    @Value("${keycloak.admin.realm}")
    private  String realm;
    @Value("${keycloak.admin.clientId}")
    private  String clientId;

    public String getAdminAccessToken() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id",clientId );
        params.add("username", username);
        params.add("password", password);
        params.add("grant_type", "password");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        ResponseEntity<Map> response = restTemplate.postForEntity(
                url,
                entity,
                Map.class);
        return response.getBody().get("access_token").toString();
    }

    public String createUser(String token, UserRequestDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", dto.getPassword());
        credentials.put("temporary", false);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", dto.getUsername());
        userPayload.put("enabled", true);
        userPayload.put("email", dto.getEmail());
        userPayload.put("firstName", dto.getFirstName());
        userPayload.put("lastName", dto.getLastName());
        userPayload.put("credentials", List.of(credentials));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userPayload, headers);

        String url = serverUrl + "/admin/realms/" + realm + "/users";

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                entity,
                String.class);

        if (!response.getStatusCode().equals(HttpStatus.CREATED)){
            throw new RuntimeException("failed to create user, " + response.getBody());
        }

        URI location = response.getHeaders().getLocation();
        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
