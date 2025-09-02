package com.ecommerce.cart.service;

import com.ecommerce.cart.dto.CartItemRequest;
import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.cart.dto.ProductResponseDto;
import com.ecommerce.cart.dto.Response;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.mapper.CartItemMapper;
import com.ecommerce.cart.client.ProductServiceClient;
import com.ecommerce.cart.repo.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductServiceClient productServiceClient;
    private final CartItemRepository repository;
    private final CartItemMapper mapper;
    public Response getCart(long userId) {
        if (!repository.existsByUserId(userId))
            return new Response(false, "CART IS EMPTY");
        List<CartItem> cartItemList = repository.findAllByUserId(userId);
        BigDecimal totalSum = cartItemList.stream().map(CartItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        CartResponse cartResponse = new CartResponse(
                cartItemList.size(),
                totalSum,
                mapper.toCartItemResponseList(cartItemList)
        );
        return new Response(true, cartResponse);
    }

    public Response addItem(long userId, CartItemRequest body) {
        Response response = productServiceClient.getOne(body.getProductId()).getBody();
        if (!response.isSuccess())
            return response;
        ProductResponseDto productResponseDto = (ProductResponseDto) response.getObject();
        Optional<CartItem> optionalCartItem = repository.findByUserIdAndProductId(userId, body.getProductId());
        CartItem cartItem = optionalCartItem.orElseGet(CartItem::new);

        cartItem.setUserId(userId);
        cartItem.setProductName(productResponseDto.getName());
        cartItem.setProductId(productResponseDto.getId());
        cartItem.setUnitPrice(productResponseDto.getPrice());
        cartItem.setQuantity(body.getQuantity());
        cartItem.setLineTotal(productResponseDto.getPrice().multiply(BigDecimal.valueOf(body.getQuantity())));
        repository.save(cartItem);
        return new Response(true,"ITEM ADDED");
    }

    public Response removeItem(long userId, long productId) {
        if (repository.existsByUserIdAndProductId(userId, productId)){
            repository.deleteByUserIdAndProductId(userId, productId);
            return new Response(true, "DELETED");
        }
        return new Response(false, "NOT FOUND");
    }
}
