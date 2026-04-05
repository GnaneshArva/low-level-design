package parking.fee;

import java.time.Duration;
import java.time.LocalDateTime;

public final class HourlyFeeStrategy implements FeeStrategy {

    private final double ratePerHour;

    public HourlyFeeStrategy(final double ratePerHour) {
        if (ratePerHour < 0) throw new IllegalArgumentException("Rate cannot be negative");
        this.ratePerHour = ratePerHour;
    }

    @Override
    public double calculate(final LocalDateTime entryTime, final LocalDateTime exitTime) {
        final long minutes = Duration.between(entryTime, exitTime).toMinutes();
        // Minimum 1 hour billing; ceil to next hour
        final long hours = Math.max(1, (long) Math.ceil(minutes / 60.0));
        return hours * ratePerHour;
    }
}
