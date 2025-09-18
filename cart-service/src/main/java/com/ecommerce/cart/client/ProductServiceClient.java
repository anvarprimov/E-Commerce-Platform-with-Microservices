package com.ecommerce.cart.client;

import com.ecommerce.cart.config.FeignSecurityConfig;
import com.ecommerce.cart.dto.ProductResponseDto;
import com.ecommerce.cart.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", configuration = FeignSecurityConfig.class)
public interface ProductServiceClient {
    @GetMapping("/api/products/{id}")
    ResponseEntity<Response<ProductResponseDto>> getOne(@PathVariable Long id);
}
