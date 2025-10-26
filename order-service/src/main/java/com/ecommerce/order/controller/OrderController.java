package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.PageResponse;
import com.ecommerce.order.dto.Response;
import com.ecommerce.order.service.OrderService;
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
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String test() {
        return "order port: " + port;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody OrderRequestDto dto) {
        String userId = jwt.getClaim("sub");
        Response<Object> response = orderService.create(userId, dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> getOne(@PathVariable long id) {
        Response<OrderResponseDto> response = orderService.getOne(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PageResponse getByUser(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String userId = jwt.getClaim("sub");
        return orderService.getByUser(userId, page, size);
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> cancel(@PathVariable Long id) {
        Response<Object> response = orderService.cancel(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
