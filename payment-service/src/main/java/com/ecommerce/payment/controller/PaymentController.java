package com.ecommerce.payment.controller;

import com.ecommerce.payment.dto.PaymentRequestDto;
import com.ecommerce.payment.dto.PaymentResponseDto;
import com.ecommerce.payment.dto.PaymentStatusDto;
import com.ecommerce.payment.dto.Response;
import com.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String test() {
        return "order port: " + port;
    }

    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> createPayment(@Valid @RequestBody PaymentRequestDto dto) {
        Response<PaymentResponseDto> response = paymentService.createPayment(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> getByPayment(@PathVariable String paymentId) {
        Response<PaymentResponseDto> response = paymentService.getByPayment(paymentId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> getByOrder(@PathVariable Long orderId) {
        Response<List<PaymentResponseDto>> response = paymentService.getByOrder(orderId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/status/{paymentId}")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'USER')")
    public HttpEntity<?> getStatus(@PathVariable String paymentId) {
        Response<PaymentStatusDto> response = paymentService.getStatus(paymentId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
