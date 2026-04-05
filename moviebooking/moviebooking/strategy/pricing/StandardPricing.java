package moviebooking.strategy.pricing;

import moviebooking.model.Seat;
import moviebooking.model.SeatType;
import moviebooking.model.Show;

public class StandardPricing implements PricingStrategy {

    private static final double REGULAR_PRICE  = 150.0;
    private static final double PREMIUM_PRICE  = 250.0;
    private static final double RECLINER_PRICE = 400.0;

    @Override
    public double calculatePrice(Seat seat, Show show) {
        return basePrice(seat.getSeatType());
    }

    protected double basePrice(SeatType type) {
        return switch (type) {
            case REGULAR  -> REGULAR_PRICE;
            case PREMIUM  -> PREMIUM_PRICE;
            case RECLINER -> RECLINER_PRICE;
        };
    }
}
