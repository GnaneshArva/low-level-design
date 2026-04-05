
package com.food.payment;

public class UpiPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Paid via UPI: " + amount);
        return true;
    }
}
