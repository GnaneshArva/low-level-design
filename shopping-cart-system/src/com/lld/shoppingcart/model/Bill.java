package com.lld.shoppingcart.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Bill {
    private final List<CartItem> items;
    private final BigDecimal subtotal;
    private final BigDecimal discountAmount;
    private final BigDecimal taxAmount;
    private final BigDecimal totalAmount;

    private Bill(Builder builder) {
        this.items = builder.items;
        this.subtotal = builder.subtotal;
        this.discountAmount = builder.discountAmount;
        this.taxAmount = builder.taxAmount;
        this.totalAmount = builder.totalAmount;
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("============= BILL =============\n");
        sb.append(String.format("%-20s %-10s %-10s\n", "Item", "Qty", "Price"));
        sb.append("--------------------------------\n");
        for (CartItem item : items) {
            sb.append(String.format("%-20s %-10d %-10s\n", 
                item.getProduct().getName(), 
                item.getQuantity(), 
                item.getItemTotal().toString()));
        }
        sb.append("--------------------------------\n");
        sb.append(String.format("Subtotal:       %s\n", subtotal));
        sb.append(String.format("Discount:      -%s\n", discountAmount));
        sb.append(String.format("Tax:           +%s\n", taxAmount));
        sb.append("--------------------------------\n");
        sb.append(String.format("TOTAL:          %s\n", totalAmount));
        sb.append("================================");
        return sb.toString();
    }

    public static class Builder {
        private List<CartItem> items;
        private BigDecimal subtotal;
        private BigDecimal discountAmount;
        private BigDecimal taxAmount;
        private BigDecimal totalAmount;

        public Builder setItems(List<CartItem> items) {
            this.items = items;
            return this;
        }

        public Builder setSubtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
            return this;
        }

        public Builder setDiscountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public Builder setTaxAmount(BigDecimal taxAmount) {
            this.taxAmount = taxAmount;
            return this;
        }

        public Builder setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public Bill build() {
            return new Bill(this);
        }
    }
}
