package com.ecommerce.cart.client;

import com.ecommerce.cart.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    @GetMapping("/api/product/{id}")
    ResponseEntity<Response> getOne(@PathVariable Long id);
}
