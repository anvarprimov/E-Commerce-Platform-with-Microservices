package com.ecommerce.user.controller;

import com.ecommerce.user.dto.Response;
import com.ecommerce.user.dto.UserRequestDto;
import com.ecommerce.user.dto.UserResponseDto;
import com.ecommerce.user.dto.UserUpdateDto;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@RefreshScope
public class UserController {
    private final UserService userService;
    @Value("${server.port}")
    private String port;
    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String hello() {
        return "user port: " + port;
    }

    // Registration (public)
    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody @Valid UserRequestDto dto) {
        Response<Object> response = userService.register(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


    // /me (authenticated)
    @GetMapping("/me")
    public HttpEntity<?> me(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        Response<UserResponseDto> response = userService.getMe(userId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PutMapping("/me")
    public HttpEntity<?> updateMe(@AuthenticationPrincipal Jwt jwt, @RequestBody  @Valid UserUpdateDto dto) {
        String userId = jwt.getClaim("sub");
        Response<Object> response = userService.updateMe(userId, dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/me")
    public HttpEntity<?> deactivateMe(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        Response<Object> response = userService.deactivateMe(userId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
