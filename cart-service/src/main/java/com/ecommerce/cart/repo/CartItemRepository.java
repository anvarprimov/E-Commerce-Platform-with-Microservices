package com.ecommerce.cart.repo;

import com.ecommerce.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserIdAndProductId(long userId, long productId);
    boolean existsByUserIdAndProductId(long userId, long productId);
    boolean existsByUserId(long userId);
    List<CartItem> findAllByUserId(long userId);
    void deleteByUserIdAndProductId(long userId, long productId);
}
