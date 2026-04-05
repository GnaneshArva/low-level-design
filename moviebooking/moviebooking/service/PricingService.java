package moviebooking.service;

import moviebooking.model.Seat;
import moviebooking.model.Show;
import moviebooking.strategy.pricing.PricingStrategy;

import java.util.List;

/**
 * SRP: Only responsible for computing the total price.
 * DIP: Depends on PricingStrategy abstraction, not concrete implementations.
 */
public class PricingService {

    private final PricingStrategy pricingStrategy;

    public PricingService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double calculateTotal(List<Seat> seats, Show show) {
        double total = 0;
        for (Seat seat : seats) {
            total += pricingStrategy.calculatePrice(seat, show);
        }
        return total;
    }
}
