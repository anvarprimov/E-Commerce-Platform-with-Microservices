package com.ecommerce.user.controller;

import com.ecommerce.user.dto.Response;
import com.ecommerce.user.dto.UserRequestDto;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@RefreshScope
public class UserAdminController {
    private final UserService service;
    @Value("${server.port}")
    private String port;
    @GetMapping("/test")
    public String hello() {
        return "user admin port: " + port;
    }

    @PostMapping
    public HttpEntity<?> add(@RequestBody UserRequestDto dto) {
        Response response = service.add(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping
    public HttpEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(required = false) Boolean active,
                                   @RequestParam(required = false) String  sort) {
        return ResponseEntity.ok(service.getAllUsers(page, size, active, sort));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOneUser(@PathVariable Long id) {
        Response response = service.getOneUser(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        Response response = service.delete(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
