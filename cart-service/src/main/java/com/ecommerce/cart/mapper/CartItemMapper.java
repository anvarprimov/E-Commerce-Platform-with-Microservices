package com.ecommerce.cart.mapper;

import com.ecommerce.cart.dto.CartItemResponse;
import com.ecommerce.cart.entity.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemResponse toCartItemResponse(CartItem cartItem);
    List<CartItemResponse> toCartItemResponseList(List<CartItem> cartItemList);
}
