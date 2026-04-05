package parking.slot;

import parking.vehicle.Vehicle;
import parking.vehicle.VehicleType;

// SRP: manages only the state of a single parking.slot
public final class ParkingSlot {

    private final String slotId;
    private final VehicleType slotType;  // parking.slot only accepts matching parking.vehicle type
    private final int floor;
    private final int position;          // used by parking.allocation strategies for ordering

    private SlotState state;
    private Vehicle assignedVehicle;

    public ParkingSlot(final String slotId, final VehicleType slotType,
                       final int floor, final int position) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.floor = floor;
        this.position = position;
        this.state = new AvailableState();
    }

    public boolean isAvailable() {
        return state instanceof AvailableState;
    }

    public void assign(final Vehicle vehicle) {
        state.assign(this, vehicle);
    }

    public void release() {
        state.release(this);
    }

    // Package-private: only state objects need to mutate these
    void setState(final SlotState state) { this.state = state; }
    void setVehicle(final Vehicle vehicle) { this.assignedVehicle = vehicle; }

    public String getSlotId()            { return slotId; }
    public VehicleType getSlotType()     { return slotType; }
    public int getFloor()                { return floor; }
    public int getPosition()             { return position; }
    public SlotState getState()          { return state; }
    public Vehicle getAssignedVehicle()  { return assignedVehicle; }

    @Override
    public String toString() {
        return String.format("Slot[%s | %s | %s | floor=%d pos=%d]",
                slotId, slotType, state.name(), floor, position);
    }
}
