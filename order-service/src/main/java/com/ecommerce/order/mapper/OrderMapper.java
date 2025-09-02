package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderItemResponseDto;
import com.ecommerce.order.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);
    List<OrderItemResponseDto> toOrderItemResponseDtoList(List<OrderItem> orderItemList);
}
