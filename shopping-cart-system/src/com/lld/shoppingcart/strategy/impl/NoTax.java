package com.lld.shoppingcart.strategy.impl;

import com.lld.shoppingcart.strategy.TaxStrategy;
import java.math.BigDecimal;

public class NoTax implements TaxStrategy {
    @Override
    public BigDecimal calculateTax(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
}
