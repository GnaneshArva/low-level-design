package moviebooking.factory;

import moviebooking.exception.UnsupportedPaymentModeException;
import moviebooking.strategy.payment.CreditCardPayment;
import moviebooking.strategy.payment.NetBankingPayment;
import moviebooking.strategy.payment.PaymentStrategy;
import moviebooking.strategy.payment.UpiPayment;

/**
 * Factory Pattern: Decouples payment moviebooking.strategy creation from the client.
 * OCP: New payment modes → add a case here, nothing else changes.
 */
public class PaymentFactory {

    private PaymentFactory() { /* utility - not instantiable */ }

    public static PaymentStrategy create(String mode, String detail) {
        if (mode == null) throw new IllegalArgumentException("Payment mode required");
        return switch (mode.toUpperCase()) {
            case "CREDIT_CARD"  -> new CreditCardPayment(detail);
            case "UPI"          -> new UpiPayment(detail);
            case "NET_BANKING"  -> new NetBankingPayment(detail);
            default             -> throw new UnsupportedPaymentModeException("Unknown payment mode: " + mode);
        };
    }
}
