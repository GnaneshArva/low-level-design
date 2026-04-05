package moviebooking.strategy.pricing;

import moviebooking.model.Seat;
import moviebooking.model.Show;

import java.time.DayOfWeek;

/**
 * OCP demonstration: adds weekend surge without touching StandardPricing.
 * LSP: substitutable wherever PricingStrategy is expected.
 */
public class WeekendPricing extends StandardPricing {

    private static final double WEEKEND_MULTIPLIER = 1.20;

    @Override
    public double calculatePrice(Seat seat, Show show) {
        double base = super.calculatePrice(seat, show);
        DayOfWeek day = show.getStartTime().getDayOfWeek();
        boolean isWeekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        return isWeekend ? base * WEEKEND_MULTIPLIER : base;
    }
}
