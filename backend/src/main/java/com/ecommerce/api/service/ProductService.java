package com.ecommerce.api.service;

import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.Product;
import com.ecommerce.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts(String search, Long categoryId, String sortBy) {
        List<Product> products;

        if (search != null || categoryId != null) {
            products = productRepository.findByFilters(search, categoryId);
        } else {
            products = productRepository.findAll();
        }

        if (sortBy != null) {
            switch (sortBy) {
                case "price_asc" -> products.sort(Comparator.comparing(Product::getPrice));
                case "price_desc" -> products.sort(Comparator.comparing(Product::getPrice).reversed());
                case "name_asc" -> products.sort(Comparator.comparing(Product::getName));
                case "name_desc" -> products.sort(Comparator.comparing(Product::getName).reversed());
            }
        }

        return products;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }
}
