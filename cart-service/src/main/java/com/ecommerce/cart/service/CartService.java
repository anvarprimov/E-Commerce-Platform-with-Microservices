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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductServiceClient productServiceClient;
    private final CartItemRepository repository;
    private final CartItemMapper mapper;

    public Response<ProductResponseDto> addItem(String userId, CartItemRequest body) {
        Response<ProductResponseDto> response;

        try {
            response = productServiceClient.getOne(body.getProductId()).getBody();
        } catch (Exception e) {
            return Response.fail("PRODUCT NOT FOUND");
        }

        ProductResponseDto productResponseDto = response.getData();

        Optional<CartItem> optionalCartItem = repository.findByUserIdAndProductId(userId, body.getProductId());
        CartItem cartItem = optionalCartItem.orElseGet(CartItem::new);

        cartItem.setUserId(userId);
        cartItem.setProductName(productResponseDto.getName());
        cartItem.setProductId(productResponseDto.getId());
        cartItem.setUnitPrice(productResponseDto.getPrice());
        cartItem.setQuantity(body.getQuantity());
        cartItem.setLineTotal(productResponseDto.getPrice().multiply(BigDecimal.valueOf(body.getQuantity())));
        repository.save(cartItem);
        return Response.ok();
    }

    public Response<CartResponse> getCart(String userId) {
        if (!repository.existsByUserId(userId))
            return Response.fail("CART IS EMPTY");
        List<CartItem> cartItemList = repository.findAllByUserId(userId);

        BigDecimal totalSum = BigDecimal.ZERO;
        int totalItems = 0;
        for (CartItem cartItem : cartItemList) {
            totalItems += cartItem.getQuantity();
            totalSum = totalSum.add(cartItem.getLineTotal());
        }
        CartResponse cartResponse = new CartResponse(
                totalItems,
                totalSum,
                mapper.toCartItemResponseList(cartItemList)
        );
        return Response.okData(cartResponse);
    }

    @Transactional
    public Response<Object> removeItem(String userId, long productId) {
        long deleted = repository.deleteByUserIdAndProductId(userId, productId);

        return deleted > 0 ? Response.ok() : Response.fail("NOT FOUND");
    }
}
