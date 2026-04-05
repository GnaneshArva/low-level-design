package com.lld.shoppingcart;

import com.lld.shoppingcart.facade.ShoppingFacade;
import com.lld.shoppingcart.factory.PricingStrategyFactory;
import com.lld.shoppingcart.model.Bill;

public class ShoppingApplication {
    public static void main(String[] args) {
        System.out.println("=== Shopping Cart System Client ===\n");

        try {
            ShoppingFacade facade = new ShoppingFacade();

            // 1. Add Items
            facade.addItem("P001", "Laptop", 1000.00, 1);
            facade.addItem("P002", "Mouse", 50.00, 2);
            facade.addItem("P003", "Keyboard", 80.00, 1);

            // 2. Update Quantity
            facade.updateQuantity("P002", 3); // Changed Mouse to 3

            // 3. Apply Strategies
            // Apply 10% Discount
            facade.setDiscountStrategy(PricingStrategyFactory.DiscountType.PERCENTAGE, 10.0);
            
            // Apply GST (18%)
            facade.setTaxStrategy(PricingStrategyFactory.TaxType.GST);

            // 4. Checkout
            Bill bill = facade.generateBill();
            System.out.println("\n" + bill);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
