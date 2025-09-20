package com.ecommerce.order.repo;

import com.ecommerce.order.entity.Order;
import com.ecommerce.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserIdAndStatusIsNot(String userId, OrderStatus status, Pageable pageable);
    Page<Order> findAllByUserId(String userId, Pageable pageable);
    Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findAllByUserIdAndStatus(String userId, OrderStatus status, Pageable pageable);
}
