package com.ecommerce.order.controller;
import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.PageResponse;
import com.ecommerce.order.dto.Response;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/test")
    public String hello(){
        return orderService.hello();
    }
    @GetMapping("/limit")
    public String limit(){
        return orderService.limit();
    }

    @PostMapping
    public HttpEntity<?> create(@Valid @RequestBody OrderRequestDto dto) {
        Response response = orderService.create(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/{orderId}")
    public HttpEntity<?> getOne(@PathVariable long orderId) {
        Response response = orderService.getOne(orderId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping
    public PageResponse getByUser(
            @RequestParam long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.getByUser(userId, page, size);
    }

    @PostMapping("/{orderId}/cancel")
    public HttpEntity<?> cancel(@PathVariable Long orderId, @RequestParam Long userId) {
        Response response = orderService.cancel(orderId, userId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
