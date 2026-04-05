package parking.slot;

import parking.exception.ParkingException;
import parking.vehicle.Vehicle;

public final class ReservedState implements SlotState {

    @Override
    public void assign(final ParkingSlot slot, final Vehicle vehicle) {
        throw new ParkingException("Slot " + slot.getSlotId() + " is reserved");
    }

    @Override
    public void release(final ParkingSlot slot) {
        slot.setState(new AvailableState());
    }

    @Override public String name() { return "RESERVED"; }
}
