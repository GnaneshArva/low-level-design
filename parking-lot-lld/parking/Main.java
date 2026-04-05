package parking;

import parking.allocation.NearestSlotStrategy;
import parking.exception.ParkingException;
import parking.facade.ParkingService;
import parking.fee.HourlyFeeStrategy;
import parking.model.ParkingFloor;
import parking.model.ParkingLot;
import parking.slot.ParkingSlot;
import parking.ticket.Ticket;
import parking.vehicle.*;

/**
 * Demonstrates the full parking lot lifecycle:
 *   setup → park → availability report → unpark → error guards
 */
public final class Main {

    public static void main(final String[] args) {

        // ── 1. Build the parking lot ──────────────────────────────────────────
        final ParkingLot lot = new ParkingLot("LOT-HYD-01");

        final ParkingFloor floor0 = new ParkingFloor(0);
        floor0.addSlot(new ParkingSlot("F0-B1", VehicleType.BIKE,  0, 1));
        floor0.addSlot(new ParkingSlot("F0-B2", VehicleType.BIKE,  0, 2));
        floor0.addSlot(new ParkingSlot("F0-C1", VehicleType.CAR,   0, 1));
        floor0.addSlot(new ParkingSlot("F0-C2", VehicleType.CAR,   0, 2));
        floor0.addSlot(new ParkingSlot("F0-T1", VehicleType.TRUCK, 0, 1));

        final ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSlot(new ParkingSlot("F1-B1", VehicleType.BIKE,  1, 1));
        floor1.addSlot(new ParkingSlot("F1-C1", VehicleType.CAR,   1, 1));
        floor1.addSlot(new ParkingSlot("F1-C2", VehicleType.CAR,   1, 2));

        lot.addFloor(floor0);
        lot.addFloor(floor1);

        // ── 2. Wire the service ───────────────────────────────────────────────
        // DIP: swap NearestSlotStrategy ↔ FirstAvailableStrategy or
        //      HourlyFeeStrategy ↔ FlatFeeStrategy without touching any other class
        final ParkingService service = new ParkingService(
                lot,
                new NearestSlotStrategy(),
                new HourlyFeeStrategy(50.0)   // ₹50 per hour, minimum 1 hour
        );

        // ── 3. Park vehicles ──────────────────────────────────────────────────
        System.out.println("--- Parking vehicles ---");
        final Vehicle bike1  = VehicleFactory.create(VehicleType.BIKE,  "TS09-AB-1234");
        final Vehicle car1   = VehicleFactory.create(VehicleType.CAR,   "TS10-CD-5678");
        final Vehicle car2   = VehicleFactory.create(VehicleType.CAR,   "KA03-EF-9999");
        final Vehicle truck1 = VehicleFactory.create(VehicleType.TRUCK, "AP07-GH-0001");
        final Vehicle bike2  = VehicleFactory.create(VehicleType.BIKE,  "TS07-XY-4321");

        final Ticket t1 = service.park(bike1);
        final Ticket t2 = service.park(car1);
        final Ticket t3 = service.park(car2);
        final Ticket t4 = service.park(truck1);
        final Ticket t5 = service.park(bike2);

        // ── 4. Availability after parking ────────────────────────────────────
        lot.printAvailability();

        // ── 5. Unpark some vehicles ───────────────────────────────────────────
        System.out.println("--- Unparking vehicles ---");
        service.unpark(t2.getTicketId());   // car1 exits
        service.unpark(t4.getTicketId());   // truck1 exits

        // ── 6. Availability after unparking ───────────────────────────────────
        lot.printAvailability();

        // ── 7. Slot reuse: park another car in the freed parking.slot ─────────────────
        System.out.println("--- Reuse freed parking.slot ---");
        final Vehicle car3 = VehicleFactory.create(VehicleType.CAR, "MH12-ZZ-7777");
        final Ticket t6 = service.park(car3);   // should get F0-C1 (nearest, just freed)
        lot.printAvailability();

        // ── 8. Error guard: double parking ────────────────────────────────────
        System.out.println("--- Error scenarios ---");
        try {
            service.park(bike1);            // bike1 is still parked
        } catch (final ParkingException e) {
            System.out.println("[GUARD] Double-park blocked  → " + e.getMessage());
        }

        // ── 9. Error guard: invalid parking.ticket ────────────────────────────────────
        try {
            service.unpark("FAKE-9999");
        } catch (final ParkingException e) {
            System.out.println("[GUARD] Invalid parking.ticket blocked → " + e.getMessage());
        }

        // ── 10. Error guard: parking lot full for a type ──────────────────────
        try {
            // Both truck slots are full (only 1 truck parking.slot exists)
            final Vehicle truck2 = VehicleFactory.create(VehicleType.TRUCK, "TS01-TR-0002");
            service.park(truck2);
        } catch (final ParkingException e) {
            System.out.println("[GUARD] Lot-full blocked       → " + e.getMessage());
        }

        System.out.printf("%nActive tickets remaining: %d%n", service.activeTicketCount());
    }
}
