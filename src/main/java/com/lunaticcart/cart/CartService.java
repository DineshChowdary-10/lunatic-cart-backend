
package com.lunaticcart.cart;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lunaticcart.entity.Product;
import com.lunaticcart.entity.User;
import com.lunaticcart.repository.UserRepository;
import com.lunaticcart.service.ProductService;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    public CartService(
            CartRepository cartRepository,
            UserRepository userRepository,
            ProductService productService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    @Transactional
    public Cart getOrCreateCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Customer not found: " + email));

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public Cart addItem(String email, Long productId, Integer quantity) {
        validateQuantity(quantity);

        Cart cart = getOrCreateCart(email);
        Product product = productService.getProductById(productId);

        CartItemEntity existing = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        int newQuantity = quantity;

        if (existing != null) {
            newQuantity += existing.getQuantity();
        }

        if (newQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Requested quantity exceeds available stock");
        }

        if (existing != null) {
            existing.setQuantity(newQuantity);
        } else {
            CartItemEntity item = new CartItemEntity(product, quantity);
            cart.addItem(item);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItemQuantity(
            String email, Long productId, Integer quantity) {
        validateQuantity(quantity);

        Cart cart = getOrCreateCart(email);

        CartItemEntity item = cart.getItems().stream()
                .filter(cartItem ->
                        cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found in cart: " + productId));

        if (quantity > item.getProduct().getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Requested quantity exceeds available stock");
        }

        item.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(String email, Long productId) {
        Cart cart = getOrCreateCart(email);

        CartItemEntity item = cart.getItems().stream()
                .filter(cartItem ->
                        cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product not found in cart: " + productId));

        cart.removeItem(item);
        return cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public Double getTotal(String email) {
        Cart cart = getOrCreateCart(email);

        return cart.getItems().stream()
                .mapToDouble(CartItemEntity::getSubtotal)
                .sum();
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }
    }
    

    @Transactional
    public void clearCart(String email) {
        Cart cart = getOrCreateCart(email);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
