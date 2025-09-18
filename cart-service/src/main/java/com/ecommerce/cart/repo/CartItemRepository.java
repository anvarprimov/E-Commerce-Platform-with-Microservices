package com.ecommerce.cart.repo;

import com.ecommerce.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserIdAndProductId(String userId, long productId);
    boolean existsByUserId(String userId);
    List<CartItem> findAllByUserId(String userId);
    long deleteByUserIdAndProductId(String userId, long productId);
}
