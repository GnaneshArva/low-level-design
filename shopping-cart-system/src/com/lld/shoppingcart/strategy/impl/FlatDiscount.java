package com.lld.shoppingcart.strategy.impl;

import com.lld.shoppingcart.strategy.DiscountStrategy;
import java.math.BigDecimal;

public class FlatDiscount implements DiscountStrategy {
    private final BigDecimal flatAmount;

    public FlatDiscount(double flatAmount) {
        if (flatAmount < 0) {
            throw new IllegalArgumentException("Discount amount cannot be negative");
        }
        this.flatAmount = BigDecimal.valueOf(flatAmount);
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        if (amount == null) return BigDecimal.ZERO;
        return amount.subtract(flatAmount).max(BigDecimal.ZERO);
    }
}
