package parking.slot;

import parking.exception.ParkingException;
import parking.vehicle.Vehicle;

public final class OccupiedState implements SlotState {

    @Override
    public void assign(final ParkingSlot slot, final Vehicle vehicle) {
        throw new ParkingException("Slot " + slot.getSlotId() + " is already occupied");
    }

    @Override
    public void release(final ParkingSlot slot) {
        slot.setVehicle(null);
        slot.setState(new AvailableState());
    }

    @Override public String name() { return "OCCUPIED"; }
}
