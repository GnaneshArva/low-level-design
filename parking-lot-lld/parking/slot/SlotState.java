package parking.slot;

import parking.vehicle.Vehicle;

// State Pattern: each state encapsulates allowed transitions; the parking.slot delegates to its current state
public interface SlotState {
    void assign(ParkingSlot slot, Vehicle vehicle);
    void release(ParkingSlot slot);
    String name();
}
