package com.lld.shoppingcart.strategy;

import java.math.BigDecimal;

public interface TaxStrategy {
    BigDecimal calculateTax(BigDecimal amount);
}
