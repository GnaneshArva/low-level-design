package com.lld.shoppingcart.service;

import com.lld.shoppingcart.model.CartItem;
import com.lld.shoppingcart.strategy.DiscountStrategy;
import com.lld.shoppingcart.strategy.TaxStrategy;
import java.math.BigDecimal;
import java.util.Collection;

public class PricingService {
    
    public BigDecimal calculateSubtotal(Collection<CartItem> items) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : items) {
            subtotal = subtotal.add(item.getItemTotal());
        }
        return subtotal;
    }

    public BigDecimal calculateDiscount(BigDecimal amount, DiscountStrategy strategy) {
        return amount.subtract(strategy.applyDiscount(amount));
    }
    
    public BigDecimal calculateTax(BigDecimal amount, TaxStrategy strategy) {
        return strategy.calculateTax(amount);
    }
}
