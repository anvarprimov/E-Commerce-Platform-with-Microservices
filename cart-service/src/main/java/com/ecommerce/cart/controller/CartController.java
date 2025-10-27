package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.CartItemRequest;
import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.cart.dto.Response;
import com.ecommerce.cart.service.CartService;
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
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;
    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String test() {
        return "cart port: " + port;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> addItem(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CartItemRequest cartItemRequest) {
        String userId = jwt.getClaim("sub");
        Response<Object> response = service.addItem(userId, cartItemRequest);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> getCart(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");
        Response<CartResponse> response = service.getCart(userId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> removeItem(@AuthenticationPrincipal Jwt jwt, @PathVariable Long productId) {
        String userId = jwt.getClaim("sub");
        Response<Object> response = service.removeItem(userId, productId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
