package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.PageResponse;
import com.ecommerce.order.dto.Response;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;
    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    public String test() {
        return "order admin port: " + port;
    }

    @GetMapping
    public PageResponse list(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return orderService.getAll(status, userId, page, size);
    }

    @PutMapping("/status/{id}")
    public Response<Object> updateStatus(@PathVariable long id, @RequestParam OrderStatus status) {

        return orderService.updateStatus(id, status);
    }
}
