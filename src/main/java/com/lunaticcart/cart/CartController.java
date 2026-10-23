
package com.lunaticcart.cart;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lunaticcart.entity.Product;
import com.lunaticcart.entity.User;
import com.lunaticcart.order.Order;
import com.lunaticcart.order.OrderService;
import com.lunaticcart.repository.UserRepository;
import com.lunaticcart.service.ProductService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final ProductService productService;

    public CartController(
            CartService cartService,
            OrderService orderService,
            UserRepository userRepository,
            ProductService productService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    @PostMapping("/items/{productId}")
    public String addItem(
            @PathVariable Long productId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        cartService.addItem(authentication.getName(), productId, quantity);
        return "Product added to cart successfully";
    }

    @GetMapping
    public Cart getCart(Authentication authentication) {
        return cartService.getOrCreateCart(authentication.getName());
    }

    @DeleteMapping("/items/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(
            @PathVariable Long productId,
            Authentication authentication) {
        cartService.removeItem(authentication.getName(), productId);
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
    }


    @PutMapping("/items/{productId}")
    public String updateItemQuantity(
            @PathVariable Long productId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        cartService.updateItemQuantity(
                authentication.getName(), productId, quantity);
        return "Cart item quantity updated successfully";
    }

    @GetMapping("/total")
    public Double getCartTotal(Authentication authentication) {
        return cartService.getTotal(authentication.getName());
    }

    @PostMapping("/checkout")
    public Order checkout(Authentication authentication) {
        Cart cart = cartService.getOrCreateCart(authentication.getName());

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot checkout with an empty cart");
        }

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Authenticated customer not found"));

        java.util.List<CartItem> checkoutItems = cart.getItems().stream()
                .map(item -> new CartItem(
                        item.getProduct(), item.getQuantity()))
                .toList();

        Order order = orderService.createOrder(
                checkoutItems, cartService.getTotal(authentication.getName()), user);

        cartService.clearCart(authentication.getName());
        return order;
    }
}
