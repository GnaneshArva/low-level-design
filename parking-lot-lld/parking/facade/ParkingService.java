package parking.facade;

import parking.allocation.SlotAllocationStrategy;
import parking.exception.ParkingException;
import parking.fee.FeeStrategy;
import parking.model.ParkingLot;
import parking.slot.ParkingSlot;
import parking.ticket.Ticket;
import parking.vehicle.Vehicle;

import java.util.HashMap;
import java.util.Map;

/**
 * Facade Pattern: single entry-point for all parking operations.
 * Hides the complexity of parking.slot parking.allocation, state transitions, and parking.fee calculation.
 *
 * DIP: depends on FeeStrategy and SlotAllocationStrategy abstractions,
 *      not on HourlyFeeStrategy or NearestSlotStrategy directly.
 */
public final class ParkingService {

    private final ParkingLot parkingLot;
    private final SlotAllocationStrategy allocationStrategy;
    private final FeeStrategy feeStrategy;

    // ticketId → Ticket; the active-parking.ticket registry
    private final Map<String, Ticket> activeTickets  = new HashMap<>();
    // vehicleNumber → ticketId; O(1) double-park guard
    private final Map<String, String> parkedVehicles = new HashMap<>();

    public ParkingService(final ParkingLot parkingLot,
                          final SlotAllocationStrategy allocationStrategy,
                          final FeeStrategy feeStrategy) {
        this.parkingLot         = parkingLot;
        this.allocationStrategy = allocationStrategy;
        this.feeStrategy        = feeStrategy;
    }

    // ── Entry ────────────────────────────────────────────────────────────────

    /**
     * Park a parking.vehicle: allocate parking.slot → transition state → issue parking.ticket.
     *
     * @return Ticket to be retained by the driver for exit
     * @throws ParkingException if parking.vehicle already parked or no parking.slot available
     */
    public Ticket park(final Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle must not be null");
        }
        if (parkedVehicles.containsKey(vehicle.getVehicleNumber())) {
            throw new ParkingException(
                    "Vehicle already parked: " + vehicle.getVehicleNumber());
        }

        final ParkingSlot slot = allocationStrategy
                .allocate(parkingLot.getAllSlots(), vehicle.getVehicleType())
                .orElseThrow(() -> new ParkingException(
                        "Parking full — no available parking.slot for: " + vehicle.getVehicleType()));

        slot.assign(vehicle);   // state transition: Available → Occupied

        final Ticket ticket = new Ticket(vehicle, slot);
        activeTickets.put(ticket.getTicketId(), ticket);
        parkedVehicles.put(vehicle.getVehicleNumber(), ticket.getTicketId());

        System.out.printf("[ENTRY] %-20s  parking.ticket=%-8s  parking.slot=%s%n",
                vehicle, ticket.getTicketId(), slot.getSlotId());
        return ticket;
    }

    // ── Exit ─────────────────────────────────────────────────────────────────

    /**
     * Unpark a parking.vehicle: validate parking.ticket → release parking.slot → calculate parking.fee.
     *
     * @return Fee charged in rupees
     * @throws ParkingException if parking.ticket is invalid or already used
     */
    public double unpark(final String ticketId) {
        if (ticketId == null || ticketId.isBlank()) {
            throw new IllegalArgumentException("Ticket ID must not be blank");
        }

        final Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new ParkingException("Invalid or already-used parking.ticket: " + ticketId);
        }

        ticket.markExit();                          // stamps exit time
        ticket.getSlot().release();                 // state transition: Occupied → Available

        activeTickets.remove(ticketId);
        parkedVehicles.remove(ticket.getVehicle().getVehicleNumber());

        final double fee = feeStrategy.calculate(ticket.getEntryTime(), ticket.getExitTime());

        System.out.printf("[EXIT]  %-20s  parking.ticket=%-8s  parking.slot=%s  parking.fee=₹%.2f%n",
                ticket.getVehicle(), ticketId, ticket.getSlot().getSlotId(), fee);
        return fee;
    }

    // ── Query ─────────────────────────────────────────────────────────────────

    public ParkingLot getParkingLot() { return parkingLot; }

    public int activeTicketCount() { return activeTickets.size(); }
}
