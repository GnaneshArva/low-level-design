
package com.food.cart;

import com.food.model.MenuItem;
import java.util.*;

public class Cart {
    private final Map<String, CartItem> items = new HashMap<>();

    public void addItem(MenuItem item) {
        items.computeIfAbsent(item.getItemId(),
                k -> new CartItem(item, 0)).increment();
    }

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public double getTotal() {
        return items.values().stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
