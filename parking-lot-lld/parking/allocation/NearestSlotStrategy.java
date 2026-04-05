package parking.allocation;

import parking.slot.ParkingSlot;
import parking.vehicle.VehicleType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// Nearest = lowest floor first, then lowest position on that floor
public final class NearestSlotStrategy implements SlotAllocationStrategy {

    @Override
    public Optional<ParkingSlot> allocate(final List<ParkingSlot> slots,
                                          final VehicleType vehicleType) {
        return slots.stream()
                .filter(s -> s.getSlotType() == vehicleType && s.isAvailable())
                .min(Comparator.comparingInt(ParkingSlot::getFloor)
                        .thenComparingInt(ParkingSlot::getPosition));
    }
}
