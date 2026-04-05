package moviebooking.strategy.payment;

public class CreditCardPayment implements PaymentStrategy {

    private final String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment(double amount) {
        // In production: call payment gateway
        System.out.println("Processing ₹" + amount + " via Credit Card ending " + lastFour());
        return true; // simulate success
    }

    @Override
    public String getPaymentMode() { return "CREDIT_CARD"; }

    private String lastFour() {
        return cardNumber.length() >= 4
            ? cardNumber.substring(cardNumber.length() - 4)
            : cardNumber;
    }
}
