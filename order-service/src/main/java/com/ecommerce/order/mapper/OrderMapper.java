package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.OrderItemResponseDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "lineTotal", ignore = true)
    OrderItem toOrderItem(OrderItemDto orderItemDto);
    @Mapping(target = "items", ignore = true)
    OrderResponseDto toOrderResponseDto(Order order);
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);
    List<OrderItemResponseDto> toOrderItemResponseDtoList(List<OrderItem> orderItemList);
}
