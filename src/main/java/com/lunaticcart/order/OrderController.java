
package com.lunaticcart.order;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lunaticcart.entity.User;
import com.lunaticcart.repository.UserRepository;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderController(
            OrderRepository orderRepository,
            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-orders")
    public List<Order> getMyOrders(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Authenticated customer not found"));

        return orderRepository.findByUserId(user.getId());
    }
}
