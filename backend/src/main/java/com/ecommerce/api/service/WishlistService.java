package com.ecommerce.api.service;

import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.model.WishlistItem;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.WishlistItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistItemRepository wishlistItemRepository, ProductRepository productRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
    }

    public List<WishlistItem> getWishlist(String email) {
        return wishlistItemRepository.findByUserEmail(email);
    }

    public WishlistItem addToWishlist(Long productId, String email) {
        if (wishlistItemRepository.existsByProductIdAndUserEmail(productId, email)) {
            return wishlistItemRepository.findByProductIdAndUserEmail(productId, email).get();
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        WishlistItem item = new WishlistItem(product);
        item.setUserEmail(email);
        return wishlistItemRepository.save(item);
    }

    @Transactional
    public void removeFromWishlist(Long productId, String email) {
        if (!wishlistItemRepository.existsByProductIdAndUserEmail(productId, email)) {
            throw new ResourceNotFoundException("Wishlist item for product", productId);
        }
        wishlistItemRepository.deleteByProductIdAndUserEmail(productId, email);
    }
}
