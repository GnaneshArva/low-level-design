package com.lld.shoppingcart.strategy.impl;

import com.lld.shoppingcart.strategy.TaxStrategy;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class VATTax implements TaxStrategy {
    private static final BigDecimal VAT_RATE = BigDecimal.valueOf(0.10);

    @Override
    public BigDecimal calculateTax(BigDecimal amount) {
        if (amount == null) return BigDecimal.ZERO;
        return amount.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
