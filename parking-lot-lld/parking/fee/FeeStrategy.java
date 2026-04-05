package parking.fee;

import java.time.LocalDateTime;

// Strategy Pattern: parking.fee algorithm is a plug-in; adding FlatFeeStrategy never touches HourlyFeeStrategy (OCP)
public interface FeeStrategy {
    double calculate(LocalDateTime entryTime, LocalDateTime exitTime);
}
