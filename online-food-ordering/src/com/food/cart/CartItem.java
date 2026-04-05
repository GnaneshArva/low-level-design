
package com.food.cart;

import com.food.model.MenuItem;

public class CartItem {
    private final MenuItem item;
    private int quantity;

    public CartItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() { return item; }
    public int getQuantity() { return quantity; }

    public void increment() { quantity++; }

    public double getTotal() {
        return item.getPrice() * quantity;
    }
}
