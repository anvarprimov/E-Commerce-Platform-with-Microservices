package com.ecommerce.product.controller;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@RefreshScope
public class ProductController {
   private final ProductService service;
   @Value("${server.port}")
   private String port;

   @GetMapping("/test")
   public String test() {
      return "product port: " + port;
   }

   @GetMapping
   public HttpEntity<?> getALlProducts(@ModelAttribute ProductSearchRequest request) {
      Response<PageResponse> response = service.getALlProducts(request);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   @GetMapping("/{id}")
   public HttpEntity<?> getOne(@PathVariable long id) {
      Response<ProductResponseDto> response = service.getOne(id);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   // inter-service
   @PostMapping("/stock")
   public HttpEntity<?> decreaseStock(@RequestBody List<QuantityDto> requestDtoList) {
      Response<List<OrderItemDto>> response = service.decreaseStock(requestDtoList);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   @PostMapping("/refund")
   public HttpEntity<?> refund(@RequestBody List<QuantityDto> quantityDtoList) {
      Response<Object> response = service.addToStock(quantityDtoList);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }
}
