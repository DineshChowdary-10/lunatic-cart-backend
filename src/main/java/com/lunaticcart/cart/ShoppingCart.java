package com.lunaticcart.cart;

import java.util.ArrayList;
import java.util.List;

import com.lunaticcart.exception.ResourceNotFoundException;

public class ShoppingCart {
	
	private final List<CartItem> items = new ArrayList<>();
	
	public List<CartItem> getItems()
	{
		return items;
	}
	

public void addItem(CartItem newItem) {
    for (CartItem item : items) {
        if (item.getProduct().getId()
                .equals(newItem.getProduct().getId())) {

            int updatedQuantity =
                    item.getQuantity() + newItem.getQuantity();

            if (updatedQuantity >
                    item.getProduct().getStockQuantity()) {
                throw new IllegalArgumentException(
                        "Requested quantity exceeds available stock");
            }

            item.setQuantity(updatedQuantity);
            return;
        }
    }

    items.add(newItem);
}

	
	public Double getTotalPrice()
	{
		Double total = 0.0;
		for(CartItem item: items)
		{
			total+=item.getSubtotal();
		}
		return total;
	}
	
	public void clearCart()
	{
		items.clear();
	}
	
	public void removeItem(Long productId) {
	    items.removeIf(item ->
	            item.getProduct().getId().equals(productId));
	}
	
	public void updateItemQuantity(Long productId, Integer quantity) {

	    if (quantity == null || quantity <= 0) {
	        throw new IllegalArgumentException(
	                "Quantity must be greater than zero");
	    }

	    for (CartItem item : items) {
	        if (item.getProduct().getId().equals(productId)) {

	            if (quantity > item.getProduct().getStockQuantity()) {
	                throw new IllegalArgumentException(
	                        "Requested quantity exceeds available stock");
	            }

	            item.setQuantity(quantity);
	            return;
	        }
	    }

	    throw new ResourceNotFoundException(
	            "Product not found in cart with id: " + productId);
	}
	
	
	public Boolean isEmpty() {
	    return items.isEmpty();
	}
}
