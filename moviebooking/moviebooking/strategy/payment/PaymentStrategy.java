package moviebooking.strategy.payment;

/**
 * Strategy Pattern: Payment methods are interchangeable.
 * ISP: Small, focused interface — just process().
 */
public interface PaymentStrategy {
    boolean processPayment(double amount);
    String getPaymentMode();
}
