package com.ecommerce.product.controller;

import com.ecommerce.product.dto.PageResponse;
import com.ecommerce.product.dto.Response;
import com.ecommerce.product.dto.StockRequestDto;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//   @GetMapping
//   public Page<ProductResponseDto> list(
//           @RequestParam(defaultValue="0") int page,
//           @RequestParam(defaultValue="20") int size,
//           @RequestParam(required=false) String brand,
//           @RequestParam(required=false) BigDecimal minPrice,
//           @RequestParam(required=false) BigDecimal maxPrice,
//           @RequestParam(required=false) String sort // e.g. "price,asc" or "name,desc"
//   ) {
//      return service.list(new ProductFilter(categoryId, brand, status, minPrice, maxPrice),
//              PageUtils.pageRequest(page, size, sort));
//   }

   @GetMapping("/search")
   public PageResponse search(
           @RequestParam(required = false) String key,
           @RequestParam(defaultValue="0") int page,
           @RequestParam(defaultValue="20") int size
   ) {
      return service.search(key, page, size);
   }

   // inter-service
   @PostMapping("/stock")
   public HttpEntity<?> decreaseStock(@RequestBody StockRequestDto stockRequestDto) {
      Response response = service.decreaseStock(stockRequestDto);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }
}
