package com.lld.shoppingcart.facade;

import com.lld.shoppingcart.exception.EmptyCartException;
import com.lld.shoppingcart.factory.PricingStrategyFactory;
import com.lld.shoppingcart.model.Bill;
import com.lld.shoppingcart.model.CartItem;
import com.lld.shoppingcart.model.Product;
import com.lld.shoppingcart.service.PricingService;
import com.lld.shoppingcart.service.ShoppingCart;
import com.lld.shoppingcart.strategy.DiscountStrategy;
import com.lld.shoppingcart.strategy.TaxStrategy;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ShoppingFacade {
    private final ShoppingCart cart;
    private final PricingService pricingService;
    private DiscountStrategy discountStrategy;
    private TaxStrategy taxStrategy;

    public ShoppingFacade() {
        this.cart = new ShoppingCart();
        this.pricingService = new PricingService();
        // Defaults
        this.discountStrategy = PricingStrategyFactory.getDiscountStrategy(PricingStrategyFactory.DiscountType.NONE, 0);
        this.taxStrategy = PricingStrategyFactory.getTaxStrategy(PricingStrategyFactory.TaxType.NONE);
    }

    public void addItem(String id, String name, double price, int quantity) {
        Product product = new Product(id, name, BigDecimal.valueOf(price));
        cart.addItem(product, quantity);
        System.out.println("Added: " + name + " x" + quantity);
    }

    public void updateQuantity(String id, int quantity) {
        cart.updateQuantity(id, quantity);
        System.out.println("Updated quantity for " + id + " to " + quantity);
    }
    
    public void removeItem(String id) {
        cart.removeItem(id);
        System.out.println("Removed product: " + id);
    }

    public void setDiscountStrategy(PricingStrategyFactory.DiscountType type, double value) {
        this.discountStrategy = PricingStrategyFactory.getDiscountStrategy(type, value);
        System.out.println("Applied Discount Strategy: " + type);
    }

    public void setTaxStrategy(PricingStrategyFactory.TaxType type) {
        this.taxStrategy = PricingStrategyFactory.getTaxStrategy(type);
        System.out.println("Applied Tax Strategy: " + type);
    }

    public Bill generateBill() {
        if (cart.isEmpty()) {
            throw new EmptyCartException("Cannot generate bill for an empty cart.");
        }

        BigDecimal subtotal = pricingService.calculateSubtotal(cart.getItems().values());
        
        // Calculate Discount
        BigDecimal discount = discountStrategy.applyDiscount(subtotal); // This returns the *reduced* amount in my interface impl? 
        // Wait, let's check the DiscountStrategy implementation.
        // PercentageDiscount: returns amount.subtract(discountAmount). Returns the FINAL amount after discount.
        // But Bill expects "Discount Amount" (value of discount), not the discounted total.
        
        // Let's re-read the strategy interface logic.
        // PercentageDiscount.applyDiscount: returns amount - (amount * percentage / 100).
        // It returns the PRICE DATE DISCOUNT.
        // So discount AMOUNT = subtotal - applyDiscount(subtotal).
        
        BigDecimal amountAfterDiscount = discountStrategy.applyDiscount(subtotal);
        BigDecimal discountValue = subtotal.subtract(amountAfterDiscount);

        // Tax is usually applied on the discounted price.
        BigDecimal taxValue = taxStrategy.calculateTax(amountAfterDiscount);
        
        BigDecimal finalTotal = amountAfterDiscount.add(taxValue);

        return new Bill.Builder()
                .setItems(new ArrayList<>(cart.getItems().values()))
                .setSubtotal(subtotal)
                .setDiscountAmount(discountValue)
                .setTaxAmount(taxValue)
                .setTotalAmount(finalTotal)
                .build();
    }
}
