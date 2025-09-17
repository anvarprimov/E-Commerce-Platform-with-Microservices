package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.Response;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/products")
@RequiredArgsConstructor
public class ProductAdminController {
   private final ProductService service;
   @Value("${server.port}")
   private String port;

   @GetMapping("/test")
   public String test() {
      return "product admin port: " + port;
   }

   @PostMapping
   public HttpEntity<?> create(@Valid @RequestBody ProductRequestDto dto) {
      Response response = service.create(dto);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   @PutMapping("/{id}")
   public HttpEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto dto) {
      Response response = service.update(id, dto);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   @DeleteMapping("/{id}")
   public HttpEntity<?> softDelete(@PathVariable Long id) {
      Response response = service.softDelete(id);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);}

   @PutMapping("/restore/{id}")
   public HttpEntity<?> restore(@PathVariable Long id) {
      Response response = service.restore(id);
      return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
   }

   /*@PostMapping(path="/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public List<ImageDto> uploadImages(@PathVariable String id, @RequestParam("files") List<MultipartFile> files) {
      return service.addImages(id, files);
   }

   @DeleteMapping("/{id}/images/{imageId}")
   public void deleteImage(@PathVariable String id, @PathVariable String imageId) {
      service.removeImage(id, imageId);
   }

   @PostMapping(path="/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   public ImportResult importCsv(@RequestParam("file") MultipartFile file) {
      return service.importCsv(file);
   }

   @GetMapping("/export")
   public ResponseEntity<Resource> exportCsv() { return service.exportCsv(); }*/
}
