package com.ecommerce.product.controller;

import com.ecommerce.product.dto.PageResponse;
import com.ecommerce.product.dto.ProductSearchRequest;
import com.ecommerce.product.dto.Response;
import com.ecommerce.product.dto.StockRequestDto;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
      Response response = service.getALlProducts(request);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   @GetMapping("/{id}")
   public HttpEntity<?> getOne(@PathVariable long id) {
      Response response = service.getOne(id);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   // inter-service
   @PostMapping("/stock")
   public HttpEntity<?> decreaseStock(@RequestBody StockRequestDto stockRequestDto) {
      Response response = service.decreaseStock(stockRequestDto);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }
}
