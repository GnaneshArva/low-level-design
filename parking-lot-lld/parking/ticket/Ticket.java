package parking.ticket;

import parking.slot.ParkingSlot;
import parking.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

// Immutable on creation; exitTime is set on unpark (single mutation point)
public final class Ticket {

    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final LocalDateTime entryTime;

    private LocalDateTime exitTime;  // null until parking.vehicle exits

    public Ticket(final Vehicle vehicle, final ParkingSlot slot) {
        this.ticketId  = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.vehicle   = vehicle;
        this.slot      = slot;
        this.entryTime = LocalDateTime.now();
    }

    public void markExit() {
        if (exitTime != null) throw new IllegalStateException("Ticket already closed: " + ticketId);
        this.exitTime = LocalDateTime.now();
    }

    public boolean isActive() { return exitTime == null; }

    public String getTicketId()         { return ticketId; }
    public Vehicle getVehicle()         { return vehicle; }
    public ParkingSlot getSlot()        { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime()  { return exitTime; }

    @Override
    public String toString() {
        return String.format("Ticket[%s | %s | parking.slot=%s | entry=%s]",
                ticketId, vehicle, slot.getSlotId(), entryTime);
    }
}
