package com.ecommerce.api.service;

import com.ecommerce.api.model.Order;
import com.ecommerce.api.model.User;
import com.ecommerce.api.repository.OrderRepository;
import com.ecommerce.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AccountService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public User getProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<Order> getOrders(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }
}
