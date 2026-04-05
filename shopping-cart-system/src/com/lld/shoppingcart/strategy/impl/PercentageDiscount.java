package com.lld.shoppingcart.strategy.impl;

import com.lld.shoppingcart.strategy.DiscountStrategy;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscount implements DiscountStrategy {
    private final BigDecimal percentage;

    public PercentageDiscount(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = BigDecimal.valueOf(percentage);
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        if (amount == null) return BigDecimal.ZERO;
        BigDecimal discountAmount = amount.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return amount.subtract(discountAmount);
    }
}
