package moviebooking.exception;

public class UnsupportedPaymentModeException extends RuntimeException {
    public UnsupportedPaymentModeException(String message) {
        super(message);
    }
}
