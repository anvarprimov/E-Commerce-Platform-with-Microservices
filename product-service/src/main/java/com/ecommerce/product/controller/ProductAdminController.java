package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.QuantityDto;
import com.ecommerce.product.dto.Response;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/products")
@RequiredArgsConstructor
public class ProductAdminController {
   private final ProductService service;
   @Value("${server.port}")
   private String port;

   @GetMapping("/test")
   @PreAuthorize("hasRole('ADMIN')")
   public String test() {
      return "product admin port: " + port;
   }

   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public HttpEntity<?> create(@RequestBody @Valid ProductRequestDto dto) {
      Response<Object> response = service.create(dto);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public HttpEntity<?> update(@PathVariable Long id, @RequestBody @Valid ProductRequestDto dto) {
      Response<Object> response = service.update(id, dto);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public HttpEntity<?> softDelete(@PathVariable Long id) {
      Response<Object> response = service.softDelete(id);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);}

   @PutMapping("/restore/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public HttpEntity<?> restore(@PathVariable Long id) {
      Response<Object> response = service.restore(id);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   @PostMapping("/stock")
   @PreAuthorize("hasRole('ADMIN')")
   public HttpEntity<?> addToStock(@RequestBody @Valid List<QuantityDto> quantityDtoList) {
      Response<Object> response = service.addToStock(quantityDtoList);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }
}
