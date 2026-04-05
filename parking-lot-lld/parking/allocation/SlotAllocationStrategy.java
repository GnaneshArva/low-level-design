package parking.allocation;

import parking.slot.ParkingSlot;
import parking.vehicle.VehicleType;

import java.util.List;
import java.util.Optional;

// Strategy Pattern: pluggable algorithms for parking.slot selection (ISP: focused single method)
public interface SlotAllocationStrategy {
    Optional<ParkingSlot> allocate(List<ParkingSlot> slots, VehicleType vehicleType);
}
