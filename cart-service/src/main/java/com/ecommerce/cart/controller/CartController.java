package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.CartItemRequest;
import com.ecommerce.cart.dto.Response;
import com.ecommerce.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;
    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    public String test() {
        return "cart port: " + port;
    }

    // In real app, resolve from security context
    /*private Long resolveUserId(HttpServletRequest req) {
        return Long.valueOf(req.getHeader("X-User-Id"));
    }*/

    @GetMapping
    public HttpEntity<?> getCart(@RequestHeader long userId) {
        Response response = service.getCart(userId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PostMapping
    public HttpEntity<?> addItem(@RequestHeader long userId, @Valid @RequestBody CartItemRequest body) {
        Response response = service.addItem(userId, body);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/{productId}")
    public HttpEntity<?> removeItem(@RequestHeader long userId, @PathVariable Long productId) {
        Response response = service.removeItem(userId, productId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
