package com.ecommerce.order.service;

import com.ecommerce.order.client.PaymentServiceClient;
import com.ecommerce.order.client.ProductServiceClient;
import com.ecommerce.order.config.RabbitMQConfig;
import com.ecommerce.order.dto.*;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.enums.PaymentMethod;
import com.ecommerce.order.enums.PaymentStatus;
import com.ecommerce.order.event.NotificationEvent;
import com.ecommerce.order.event.PaymentStatusEvent;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.repo.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ProductServiceClient productServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    @CircuitBreaker(name = "productService")
    public void sendMessage(NotificationEvent event){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                event);
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
        order.setPaymentStatus(PaymentStatus.UNPAID);
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

        PaymentResponseDto paymentResponseDto = paymentServiceClient.createPayment(new PaymentRequestDto(
                order.getId(),
                userId,
                total,
                order.getPaymentMethod()
        ));

        order.setPaymentId(paymentResponseDto.getPaymentId());
        orderRepository.save(order);

        NotificationEvent event = new NotificationEvent(
                userId,
                "Your order has been created",
                "Order id = " + order.getId() +
                        ", order total price = " + order.getTotal() +
                        ", order creation date = " + order.getCreatedAt() +
                        ", estimated delivery date is within three business days",
                "EMAIL"
        );
        sendMessage(event);

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

    public Response<Object> cancel(long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
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

        NotificationEvent event = new NotificationEvent(
                order.getUserId(),
                "Your order has been CANCELLED",
                "Order id = " + order.getId() +
                        ", order cancellation date = " + order.getUpdatedAt() +
                        ", order total price = " + order.getTotal() +
                        ", order creation date = " + order.getCreatedAt() +
                        ", you will receive refund within three business days",
                "EMAIL"
        );
        sendMessage(event);

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
        if(order.getStatus().equals(OrderStatus.DELIVERED) || order.getStatus().equals(OrderStatus.CANCELED))
            return Response.fail("Sorry order is " + order.getStatus() + ", it is too late");
        order.setStatus(status);
        orderRepository.save(order);
        sentStatusUpdateNotification(order);
        return Response.ok();
    }

    private void sentStatusUpdateNotification(Order order) {
        NotificationEvent event = new NotificationEvent(
                order.getUserId(),
                "Order status has been changed",
                "Order id = " + order.getId() +
                        ", order total price = " + order.getTotal() +
                        ", order creation date = " + order.getCreatedAt() +
                        ", order has been " + order.getStatus().toString(),
                "EMAIL"
        );
        sendMessage(event);
    }

    public void handlePaymentStatus(PaymentStatusEvent event) {
        Optional<Order> optionalOrder = orderRepository.findById(event.getOrderId());
        if (optionalOrder.isEmpty())
                return;
        Order order = optionalOrder.get();
        order.setPaymentStatus(event.getStatus());
        if (event.getStatus().equals(PaymentStatus.PAID)){
            order.setStatus(OrderStatus.CONFIRMED);
        }
        orderRepository.save(order);
        sentStatusUpdateNotification(order);
    }
}
