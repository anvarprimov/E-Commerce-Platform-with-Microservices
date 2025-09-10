package com.ecommerce.user.controller;

import com.ecommerce.user.dto.Response;
import com.ecommerce.user.dto.UserRequestDto;
import com.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@RefreshScope
@Slf4j
public class UserController {
    private final UserService service;
//    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Value("${build.id:default}")
    private String id;
    @Value("${build.name:default}")
    private String name;
    @Value("${build.version:default}")
    private String version;
    @GetMapping("/test")
    public String hello() {
        log.info("created : {}", "info");
        log.warn("created : {}", "warn");
        log.error("created : {}", "error");
        log.trace("created : {}", "trace");
        log.debug("created : {}", "debug");
        return "id: " + id + " version: " + version + " name: " + name;
    }
    // Registration (public)
    @PostMapping
    public HttpEntity<?> register(@RequestBody UserRequestDto dto) {
        Response response = service.register(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
    @PostMapping("/admin")
    public HttpEntity<?> add(@RequestBody UserRequestDto dto) {
        Response response = service.add(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    // admin
    @GetMapping("/admin")
    public HttpEntity<?> adminList(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(required = false) Boolean active,
                                   @RequestParam(required = false) String  sort) {
        return ResponseEntity.ok(service.getAllUsers(page, size, active, sort));
    }

    @GetMapping("/admin/{id}")
    public HttpEntity<?> adminGet(@PathVariable Long id) {
        Response response = service.getOneUser(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/admin/{id}")
    public HttpEntity<?> adminDelete(@PathVariable Long id) {
        Response response = service.deleteUserByAdmin(id);
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
