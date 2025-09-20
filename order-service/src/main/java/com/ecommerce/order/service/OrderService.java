package com.ecommerce.order.service;

import com.ecommerce.order.client.ProductServiceClient;
import com.ecommerce.order.client.UserServiceClient;
import com.ecommerce.order.dto.*;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.enums.PaymentMethod;
import com.ecommerce.order.enums.PaymentStatus;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repo.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;


    @CircuitBreaker(name = "productService")
    public String hello(){
        rabbitTemplate.convertAndSend(
                exchangeName,
                routingKey,
                Map.of("orderId", "1", "status", "created"));
        return userServiceClient.hello();
    }


    public Response<Object> create(String userId, OrderRequestDto dto) {
        Response<List<OrderItemDto>> response;
        try {
            response = productServiceClient.decreaseStock(dto.getItems());
        } catch (Exception e) {
            return Response.fail("NOT ENOUGH PRODUCT");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setNotes(dto.getNotes());
        order.setPaymentMethod(PaymentMethod.CARD);
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal subTotal = BigDecimal.ZERO;
        for (OrderItemDto itemDto : response.data) {
            OrderItem item = orderMapper.toOrderItem(itemDto);
            item.setLineTotal(item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
            order.addItem(item);
            subTotal = subTotal.add(item.getLineTotal());
        }

        BigDecimal percent = BigDecimal.valueOf(0.08);
        BigDecimal tax = subTotal.multiply(percent);
        BigDecimal shippingFee = subTotal.multiply(percent);
        BigDecimal total = subTotal.add(tax).add(shippingFee);

        order.setSubtotal(subTotal);
        order.setTax(tax);
        order.setShippingFee(shippingFee);
        order.setTotal(total);
        orderRepository.save(order);
        return Response.ok();
    }

    public Response<OrderResponseDto> getOne(long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.map(order -> Response.okData(toOrderResponseDto(order))).orElse(Response.fail("NOT FOUND"));
    }

    public PageResponse getByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        return toPageResponse(orderRepository.findAllByUserIdAndStatusIsNot(userId, OrderStatus.CANCELED, pageable));
    }

    public Response<Object> cancel(long id) {Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty())
            return Response.fail("ORDER NOT FOUND");
        Order order = optionalOrder.get();
        if (!order.getStatus().equals(OrderStatus.PENDING))
            return Response.fail("IT IS TOO LATE TO CANCEL, ORDER HAS BEEN SHIPPED");
        order.setStatus(OrderStatus.CANCELED);
        List<QuantityDto> quantityDtoList = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            quantityDtoList.add(new QuantityDto(
                    item.getProductId(),
                    item.getQuantity()
            ));
        }
        try {
            productServiceClient.refund(quantityDtoList);
        } catch (Exception e) {
            return Response.fail("PRODUCT SERVER FAILED");
        }


        orderRepository.save(order);
        return Response.ok();
    }

//    by admin
    public PageResponse getAll(OrderStatus status, String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        Page<Order> orderPage;
        if (status != null && userId != null)
            orderPage = orderRepository.findAllByUserIdAndStatus(userId, status, pageable);
        else if (status != null)
            orderPage = orderRepository.findAllByStatus(status, pageable);
        else if (userId != null)
            orderPage = orderRepository.findAllByUserId(userId, pageable);
        else
            orderPage = orderRepository.findAll(pageable);
        return toPageResponse(orderPage);
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
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(order);
        responseDto.setItems(orderMapper.toOrderItemResponseDtoList(order.getItems()));
        return responseDto;
    }

    public Response<Object> updateStatus(long id, OrderStatus status) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty())
            return Response.fail("ORDER NOT FOUND");
        Order order = optionalOrder.get();
        order.setStatus(status);
        orderRepository.save(order);
        return Response.ok();
    }
}
