package com.ecommerce.api.repository;

import com.ecommerce.api.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {

    Optional<WishlistItem> findByProductId(Long productId);

    void deleteByProductId(Long productId);

    boolean existsByProductId(Long productId);

    List<WishlistItem> findByUserEmail(String userEmail);

    Optional<WishlistItem> findByProductIdAndUserEmail(Long productId, String userEmail);

    boolean existsByProductIdAndUserEmail(Long productId, String userEmail);

    void deleteByProductIdAndUserEmail(Long productId, String userEmail);
}
