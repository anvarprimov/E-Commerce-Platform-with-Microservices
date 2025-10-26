package com.ecommerce.user.controller;

import com.ecommerce.user.dto.Response;
import com.ecommerce.user.dto.UserRequestDto;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@RefreshScope
public class UserController {
    private final UserService service;
    @Value("${server.port}")
    private String port;
    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String hello() {
        return "user port: " + port;
    }
    // Registration (public)
    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody UserRequestDto dto) {
        Response response = service.register(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    /**
     * TODO
     */
    // /me (authenticated)
    /*@GetMapping("/me")
    public HttpEntity<?> me() {
        return ResponseEntity.ok(userService.getMe(current.currentUserId()));
    }

    @PatchMapping("/me")
    public HttpEntity<?> updateMe(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.updateMe(current.currentUserId(), dto));
    }

    @DeleteMapping("/me")
    public HttpEntity<?> deactivateMe() {
        userService.deactivateMe(current.currentUserId());
        return ResponseEntity.noContent().build();
    }*/
}
