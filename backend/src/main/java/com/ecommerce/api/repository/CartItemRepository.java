package com.ecommerce.api.repository;

import com.ecommerce.api.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProductId(Long productId);

    void deleteByProductId(Long productId);

    List<CartItem> findByUserEmail(String userEmail);

    Optional<CartItem> findByProductIdAndUserEmail(Long productId, String userEmail);

    void deleteByProductIdAndUserEmail(Long productId, String userEmail);

    void deleteByUserEmail(String userEmail);
}
