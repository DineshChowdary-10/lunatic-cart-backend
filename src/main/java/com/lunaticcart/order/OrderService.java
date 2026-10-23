package com.lunaticcart.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lunaticcart.cart.CartItem;
import com.lunaticcart.entity.Product;
import com.lunaticcart.entity.User;
import com.lunaticcart.service.ProductService;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(
            OrderRepository orderRepository,
            ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public Order createOrder(Double totalAmount) {
        Order order = new Order(
                LocalDateTime.now(),
                totalAmount,
                "PLACED"
        );

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Order not found with id: " + id));
    }

    @Transactional
    public Order createOrder(
            List<CartItem> cartItems,
            Double totalAmount,
            User user) {

        Order order = new Order(
                LocalDateTime.now(),
                totalAmount,
                "PLACED"
        );

        
        order.setUser(user);

        for (CartItem cartItem : cartItems) {

            Product updatedProduct = productService.reduceStock(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity()
            );

            OrderItem orderItem = new OrderItem(
                    order,
                    updatedProduct,
                    cartItem.getQuantity(),
                    updatedProduct.getPrice()
            );

            order.addItem(orderItem);
        }

        return orderRepository.save(order);
    }
}