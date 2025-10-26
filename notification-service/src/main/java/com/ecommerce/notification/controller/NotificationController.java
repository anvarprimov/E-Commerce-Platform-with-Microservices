package com.ecommerce.notification.controller;

import com.ecommerce.notification.dto.PageResponse;
import com.ecommerce.notification.dto.Response;
import com.ecommerce.notification.dto.SendRequest;
import com.ecommerce.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    public String test() {
        return "notification port: " + port;
    }

    private final NotificationService service;

    @PostMapping()
    public HttpEntity<?> send(@RequestBody @Valid SendRequest req){
        Response<Object> response = service.send(req);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/me")
    public PageResponse myNotifications(@AuthenticationPrincipal Jwt jwt,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size){
        String userId = jwt.getClaim("sub");
        return service.getAll(userId, page, size);
    }

    @PostMapping("/read/{id}")
    public void markRead(@PathVariable long id, @AuthenticationPrincipal Jwt jwt){
        String userId = jwt.getClaim("sub");
        service.markRead(id, userId);
    }
}
