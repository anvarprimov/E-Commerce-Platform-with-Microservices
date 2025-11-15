package com.ecommerce.order.client;

import com.ecommerce.order.config.FeignSecurityConfig;
import com.ecommerce.order.dto.PaymentRequestDto;
import com.ecommerce.order.dto.PaymentResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "payment-service", configuration = FeignSecurityConfig.class)
public interface PaymentServiceClient {
    @PostMapping("/api/payments")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'USER')")
    PaymentResponseDto createPayment(@Valid @RequestBody PaymentRequestDto dto);
}
