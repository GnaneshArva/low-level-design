package moviebooking.strategy.payment;

public class NetBankingPayment implements PaymentStrategy {

    private final String bankName;

    public NetBankingPayment(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing ₹" + amount + " via Net Banking: " + bankName);
        return true;
    }

    @Override
    public String getPaymentMode() { return "NET_BANKING"; }
}
