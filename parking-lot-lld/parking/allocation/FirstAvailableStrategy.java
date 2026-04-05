package parking.allocation;

import parking.slot.ParkingSlot;
import parking.vehicle.VehicleType;

import java.util.List;
import java.util.Optional;

// Useful when list is already sorted by insertion order (e.g. nearest entry gate first)
public final class FirstAvailableStrategy implements SlotAllocationStrategy {

    @Override
    public Optional<ParkingSlot> allocate(final List<ParkingSlot> slots,
                                          final VehicleType vehicleType) {
        return slots.stream()
                .filter(s -> s.getSlotType() == vehicleType && s.isAvailable())
                .findFirst();
    }
}
