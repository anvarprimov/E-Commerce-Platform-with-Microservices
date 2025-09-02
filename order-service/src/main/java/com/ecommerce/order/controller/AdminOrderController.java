package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.PageResponse;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public PageResponse list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return orderService.getAll(page, size);
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponseDto updateStatus(@PathVariable long orderId, @RequestParam OrderStatus status) {
        return orderService.updateStatus(orderId, status);
    }
}
