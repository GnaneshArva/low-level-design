package parking.slot;

import parking.exception.ParkingException;
import parking.vehicle.Vehicle;

public final class AvailableState implements SlotState {

    @Override
    public void assign(final ParkingSlot slot, final Vehicle vehicle) {
        slot.setVehicle(vehicle);
        slot.setState(new OccupiedState());
    }

    @Override
    public void release(final ParkingSlot slot) {
        throw new ParkingException("Slot " + slot.getSlotId() + " is already available");
    }

    @Override public String name() { return "AVAILABLE"; }
}
