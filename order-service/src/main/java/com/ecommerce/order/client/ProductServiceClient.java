package com.ecommerce.order.client;

import com.ecommerce.order.config.FeignSecurityConfig;
import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.QuantityDto;
import com.ecommerce.order.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", configuration = FeignSecurityConfig.class)
public interface ProductServiceClient {
    @PostMapping("/api/products/stock")
    Response<List<OrderItemDto>> decreaseStock(@RequestBody List<QuantityDto> requestDtoList);
    @PostMapping("/api/products/refund")
    Response<Object> refund(@RequestBody List<QuantityDto> requestDtoList);
}
