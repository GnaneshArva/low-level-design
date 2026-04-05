package com.lld.shoppingcart.service;

import com.lld.shoppingcart.exception.InvalidProductException;
import com.lld.shoppingcart.model.CartItem;
import com.lld.shoppingcart.model.Product;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<String, CartItem> items;

    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        if (product == null) {
            throw new InvalidProductException("Cannot add null product to cart");
        }
        if (items.containsKey(product.getId())) {
            CartItem existingItem = items.get(product.getId());
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity));
        }
    }

    public void updateQuantity(String productId, int quantity) {
        if (!items.containsKey(productId)) {
            throw new InvalidProductException("Product not found in cart: " + productId);
        }
        items.get(productId).setQuantity(quantity);
    }

    public void removeItem(String productId) {
        if (!items.containsKey(productId)) {
            throw new InvalidProductException("Product not found in cart: " + productId);
        }
        items.remove(productId);
    }

    public Map<String, CartItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
