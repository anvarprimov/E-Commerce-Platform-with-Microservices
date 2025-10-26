package com.ecommerce.order.controller;

import com.ecommerce.order.dto.PageResponse;
import com.ecommerce.order.dto.Response;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {

    private final OrderService orderService;
    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String test() {
        return "order admin port: " + port;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse list(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return orderService.getAll(status, userId, page, size);
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public HttpEntity<?> updateStatus(@PathVariable long id, @RequestParam OrderStatus status) {
        Response<java.lang.Object> response = orderService.updateStatus(id, status);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
