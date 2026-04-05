package parking.fee;

import java.time.LocalDateTime;

public final class FlatFeeStrategy implements FeeStrategy {

    private final double flatFee;

    public FlatFeeStrategy(final double flatFee) {
        if (flatFee < 0) throw new IllegalArgumentException("Fee cannot be negative");
        this.flatFee = flatFee;
    }

    @Override
    public double calculate(final LocalDateTime entryTime, final LocalDateTime exitTime) {
        return flatFee;
    }
}
