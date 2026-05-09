package com.ecommerce.api.service;

import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Category;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.model.Promotion;
import com.ecommerce.api.repository.ProductRepository;
import com.ecommerce.api.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    public PromotionService(PromotionRepository promotionRepository, ProductRepository productRepository) {
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
    }

    public List<Promotion> getAll() {
        return promotionRepository.findAll();
    }

    public List<Promotion> getActive() {
        return promotionRepository.findByActiveTrue();
    }

    public Promotion getById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion", id));
    }

    public List<Long> getAffectedProductIds(Promotion promotion) {
        List<Long> productIds = new ArrayList<>();

        // Add direct product IDs
        for (Product product : promotion.getProducts()) {
            productIds.add(product.getId());
        }

        // Add product IDs from linked categories
        for (Category category : promotion.getCategories()) {
            List<Product> categoryProducts = productRepository.findByCategoryId(category.getId());
            for (Product product : categoryProducts) {
                if (!productIds.contains(product.getId())) {
                    productIds.add(product.getId());
                }
            }
        }

        return productIds;
    }

    public Integer getDiscountForProduct(Long productId) {
        List<Promotion> activePromotions = promotionRepository.findByActiveTrue();
        int maxDiscount = 0;

        for (Promotion promotion : activePromotions) {
            // Check if the product is directly linked to this promotion
            boolean directMatch = promotion.getProducts().stream()
                    .anyMatch(p -> p.getId().equals(productId));

            // Check if the product's category is linked to this promotion
            boolean categoryMatch = false;
            if (!directMatch) {
                Product product = productRepository.findById(productId).orElse(null);
                if (product != null && product.getCategory() != null) {
                    categoryMatch = promotion.getCategories().stream()
                            .anyMatch(c -> c.getId().equals(product.getCategory().getId()));
                }
            }

            if (directMatch || categoryMatch) {
                maxDiscount = Math.max(maxDiscount, promotion.getDiscountPercentage());
            }
        }

        return maxDiscount;
    }
}
