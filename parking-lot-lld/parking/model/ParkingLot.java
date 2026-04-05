package parking.model;

import parking.slot.ParkingSlot;
import parking.vehicle.VehicleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// SRP: owns floors; exposes a flat parking.slot view for strategies
public final class ParkingLot {

    private final String lotId;
    private final List<ParkingFloor> floors;

    public ParkingLot(final String lotId) {
        this.lotId  = lotId;
        this.floors = new ArrayList<>();
    }

    public void addFloor(final ParkingFloor floor) {
        floors.add(floor);
    }

    /**
     * Flat list fed to SlotAllocationStrategy.
     * Strategy is decoupled from floor structure — it only sees slots.
     */
    public List<ParkingSlot> getAllSlots() {
        return floors.stream()
                .flatMap(f -> f.getSlots().stream())
                .collect(Collectors.toList());
    }

    public List<ParkingFloor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    public void printAvailability() {
        System.out.println("\n=== Availability Report: " + lotId + " ===");
        for (final ParkingFloor floor : floors) {
            System.out.printf("  Floor %d:%n", floor.getFloorNumber());
            for (final VehicleType type : VehicleType.values()) {
                System.out.printf("    %-5s → %d parking.slot(s) available%n",
                        type, floor.countAvailable(type));
            }
        }
        System.out.println();
    }

    public String getLotId() { return lotId; }
}
