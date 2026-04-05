package com.lld.shoppingcart.strategy.impl;

import com.lld.shoppingcart.strategy.DiscountStrategy;
import java.math.BigDecimal;

public class NoDiscount implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        if (amount == null) return BigDecimal.ZERO;
        return amount;
    }
}
