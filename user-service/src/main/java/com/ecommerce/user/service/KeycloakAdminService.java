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
    @Value("${keycloak.admin.clientUuid}")
    private  String clientUuid;
    @Value("${keycloak.admin.defaultRole}")
    private  String defaultRole;

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

    private Map<String, Object> getRealmRoleRepresentation(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = serverUrl + "/admin/realms/" + realm + "/clients/" + clientUuid + "/roles/" + defaultRole;
        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class);
        return response.getBody();
    }

    public boolean assignRealmRoleToUser(String token, String userId) {
        Map<String, Object> roleRep = getRealmRoleRepresentation(token);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(List.of(roleRep), headers);
        String url = serverUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/clients/" + clientUuid;

        ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);
        return response.getStatusCode().is2xxSuccessful();
    }
}
