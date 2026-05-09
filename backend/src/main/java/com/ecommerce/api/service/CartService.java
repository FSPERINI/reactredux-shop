package com.ecommerce.api.service;

import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.repository.CartItemRepository;
import com.ecommerce.api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCartItems(String email) {
        return cartItemRepository.findByUserEmail(email);
    }

    public CartItem addToCart(Long productId, Integer quantity, String email) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        Optional<CartItem> existing = cartItemRepository.findByProductIdAndUserEmail(productId, email);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        }

        CartItem item = new CartItem(product, quantity, email);
        return cartItemRepository.save(item);
    }

    public CartItem updateCartItem(Long productId, Integer quantity, String email) {
        CartItem item = cartItemRepository.findByProductIdAndUserEmail(productId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item for product", productId));
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void removeCartItem(Long productId, String email) {
        if (cartItemRepository.findByProductIdAndUserEmail(productId, email).isEmpty()) {
            throw new ResourceNotFoundException("Cart item for product", productId);
        }
        cartItemRepository.deleteByProductIdAndUserEmail(productId, email);
    }

    @Transactional
    public void clearCart(String email) {
        cartItemRepository.deleteByUserEmail(email);
    }
}
