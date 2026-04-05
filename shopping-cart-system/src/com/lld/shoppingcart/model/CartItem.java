package com.lld.shoppingcart.model;

import com.lld.shoppingcart.exception.InvalidQuantityException;
import java.math.BigDecimal;

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        validateQuantity(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    public BigDecimal getItemTotal() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
