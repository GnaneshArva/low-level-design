package com.lld.shoppingcart.factory;

import com.lld.shoppingcart.strategy.DiscountStrategy;
import com.lld.shoppingcart.strategy.TaxStrategy;
import com.lld.shoppingcart.strategy.impl.*;

public class PricingStrategyFactory {

    public enum DiscountType {
        PERCENTAGE, FLAT, NONE
    }

    public enum TaxType {
        GST, VAT, NONE
    }

    public static DiscountStrategy getDiscountStrategy(DiscountType type, double value) {
        switch (type) {
            case PERCENTAGE:
                return new PercentageDiscount(value);
            case FLAT:
                return new FlatDiscount(value);
            case NONE:
            default:
                return new NoDiscount();
        }
    }

    public static TaxStrategy getTaxStrategy(TaxType type) {
        switch (type) {
            case GST:
                return new GSTTax();
            case VAT:
                return new VATTax();
            case NONE:
            default:
                return new NoTax();
        }
    }
}
