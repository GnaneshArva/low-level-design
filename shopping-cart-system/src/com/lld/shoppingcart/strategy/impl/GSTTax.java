package com.lld.shoppingcart.strategy.impl;

import com.lld.shoppingcart.strategy.TaxStrategy;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class GSTTax implements TaxStrategy {
    private static final BigDecimal GST_RATE = BigDecimal.valueOf(0.18);

    @Override
    public BigDecimal calculateTax(BigDecimal amount) {
        if (amount == null) return BigDecimal.ZERO;
        return amount.multiply(GST_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
