package com.ecommerce.api.service;

import com.ecommerce.api.dto.DeliveryInfo;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.model.CartItem;
import com.ecommerce.api.model.Order;
import com.ecommerce.api.model.OrderItem;
import com.ecommerce.api.repository.CartItemRepository;
import com.ecommerce.api.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Order createOrder(DeliveryInfo deliveryInfo, String email) {
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(email);

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty. Cannot create order.");
        }

        Order order = new Order();
        order.setFullName(deliveryInfo.getFullName());
        order.setEmail(deliveryInfo.getEmail());
        order.setAddress(deliveryInfo.getAddress());
        order.setCity(deliveryInfo.getCity());
        order.setZipCode(deliveryInfo.getZipCode());
        order.setUserEmail(email);

        double total = 0;
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                    order,
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()
            );
            order.getItems().add(orderItem);
            total += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteByUserEmail(email);

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }
}
