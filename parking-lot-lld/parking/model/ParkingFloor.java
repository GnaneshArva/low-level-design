package parking.model;

import parking.slot.ParkingSlot;
import parking.vehicle.VehicleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// SRP: owns a collection of slots on one floor; no business logic here
public final class ParkingFloor {

    private final int floorNumber;
    private final List<ParkingSlot> slots;

    public ParkingFloor(final int floorNumber) {
        this.floorNumber = floorNumber;
        this.slots = new ArrayList<>();
    }

    public void addSlot(final ParkingSlot slot) {
        slots.add(slot);
    }

    public List<ParkingSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public long countAvailable(final VehicleType type) {
        return slots.stream()
                .filter(s -> s.getSlotType() == type && s.isAvailable())
                .count();
    }

    public int getFloorNumber() { return floorNumber; }
}
