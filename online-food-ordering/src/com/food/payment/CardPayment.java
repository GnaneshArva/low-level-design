
package com.food.payment;

public class CardPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Paid via Card: " + amount);
        return true;
    }
}
