package moviebooking.strategy.payment;

public class UpiPayment implements PaymentStrategy {

    private final String upiId;

    public UpiPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing ₹" + amount + " via UPI: " + upiId);
        return true;
    }

    @Override
    public String getPaymentMode() { return "UPI"; }
}
