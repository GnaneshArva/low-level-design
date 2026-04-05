package moviebooking.strategy.pricing;

import moviebooking.model.Seat;
import moviebooking.model.Show;

/**
 * Strategy Pattern: Pricing rules are swappable without modifying Booking logic.
 * OCP: New pricing (e.g., holiday surge) = new class, no existing changes.
 */
public interface PricingStrategy {
    double calculatePrice(Seat seat, Show show);
}
