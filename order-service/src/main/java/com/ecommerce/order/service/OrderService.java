package com.ecommerce.order.service;

import com.ecommerce.order.client.UserServiceClient;
import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.PageResponse;
import com.ecommerce.order.dto.Response;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repo.OrderItemRepository;
import com.ecommerce.order.repo.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserServiceClient userServiceClient;

//    @CircuitBreaker(name = "productService")
    public String hello(){
        return userServiceClient.hello();
    }

    public Response create(OrderRequestDto dto) {

        return null;
    }

    public Response getOne(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.map(order -> new Response(true, toOrderResponseDto(order))).orElse(new Response(false, "NOT FOUND"));
    }

    public PageResponse getByUser(long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        return toPageResponse(orderRepository.findAllByUserId(userId, pageable));
    }

//    todo
    public Response cancel(Long orderId, Long userId) {
        return null;
    }

    /**
     * by admin
     */
    public PageResponse getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        return toPageResponse(orderRepository.findAll(pageable));
    }

    private PageResponse toPageResponse(Page<Order> orderPage){
        return new PageResponse(
                orderPage.getContent().stream().map(this::toOrderResponseDto).toList(),
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages()
        );
    }

    private OrderResponseDto toOrderResponseDto(Order order){
        return new OrderResponseDto(
                order.getId(),
                order.getUserId(),
                orderMapper.toOrderItemResponseDtoList(order.getItems()),
                order.getTotal(),
                order.getNotes(),
                order.getCreatedAt()
        );
    }

    public OrderResponseDto updateStatus(long orderId, OrderStatus status) {
        return null;
    }
}
