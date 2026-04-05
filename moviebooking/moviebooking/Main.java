package moviebooking;

import moviebooking.facade.BookingFacade;
import moviebooking.factory.PaymentFactory;
import moviebooking.model.*;
import moviebooking.service.BookingService;
import moviebooking.service.PricingService;
import moviebooking.service.SeatLockManager;
import moviebooking.service.ShowService;
import moviebooking.strategy.payment.PaymentStrategy;
import moviebooking.strategy.pricing.WeekendPricing;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Entry point demonstrating the full booking lifecycle:
 * setup → search → select → lock → pay → confirm → cancel
 */
public class Main {

    public static void main(String[] args) {

        // ── 1. Build the domain: Theater → Screen → Seats ──────────────────

        List<Seat> seats = List.of(
            new Seat("S1", "A", 1, SeatType.REGULAR),
            new Seat("S2", "A", 2, SeatType.REGULAR),
            new Seat("S3", "B", 1, SeatType.PREMIUM),
            new Seat("S4", "B", 2, SeatType.PREMIUM),
            new Seat("S5", "C", 1, SeatType.RECLINER)
        );

        Screen screen = new Screen("SCR1", "Screen 1", seats);

        Theater theater = new Theater("TH1", "PVR Jubilee Hills", "Hyderabad");
        theater.addScreen(screen);

        Movie movie = new Movie("M1", "Kalki 2898-AD", 181);

        // Saturday show — WeekendPricing will apply a 20% surcharge
        LocalDateTime showTime = LocalDateTime.now().plusDays(7); // upcoming show
        Show show = new Show("SH1", movie, screen, showTime);

        // ── 2. Wire services (DIP: inject abstractions) ──────────────────────

        ShowService showService       = new ShowService();
        SeatLockManager lockManager   = new SeatLockManager();
        PricingService pricingService = new PricingService(new WeekendPricing());
        BookingService bookingService = new BookingService(lockManager, pricingService);

        showService.addShow(show);

        BookingFacade facade = new BookingFacade(showService, bookingService, theater.getName());

        // ── 3. Search shows ──────────────────────────────────────────────────

        System.out.println("=== Searching shows for 'Kalki 2898-AD' ===");
        List<Show> found = facade.searchShows("Kalki 2898-AD");
        found.forEach(s -> System.out.println("  Found show: " + s.getShowId()
            + " | " + s.getMovie().getName()
            + " | " + s.getStartTime()));

        // ── 4. View available seats ──────────────────────────────────────────

        System.out.println("\n=== Available Seats ===");
        facade.getAvailableSeats("SH1").forEach(s ->
            System.out.println("  " + s));

        // ── 5. Initiate booking (locks seats) ────────────────────────────────

        System.out.println("\n=== Initiating Booking: S1, S3 ===");
        Booking booking = facade.initiateBooking("SH1", Arrays.asList("S1", "S3"));
        System.out.println("  Booking created: " + booking);

        // ── 6. Show available seats (S1, S3 should be LOCKED now) ────────────

        System.out.println("\n=== Available Seats after lock ===");
        facade.getAvailableSeats("SH1").forEach(s ->
            System.out.println("  " + s));

        // ── 7. Confirm booking via UPI payment ───────────────────────────────

        System.out.println("\n=== Confirming Booking via UPI ===");
        PaymentStrategy upi = PaymentFactory.create("UPI", "gnanesh@okaxis");
        BookingReceipt receipt = facade.confirmBooking(booking.getBookingId(), upi);
        System.out.println(receipt);

        // ── 8. Attempt double-booking (should throw SeatNotAvailableException) ─

        System.out.println("=== Attempting to double-book S1 ===");
        try {
            facade.initiateBooking("SH1", List.of("S1"));
        } catch (Exception e) {
            System.out.println("  Expected error: " + e.getMessage());
        }

        // ── 9. A fresh booking and then cancel it ────────────────────────────

        System.out.println("\n=== Fresh Booking: S2 and S4 ===");
        Booking booking2 = facade.initiateBooking("SH1", Arrays.asList("S2", "S4"));
        PaymentStrategy card = PaymentFactory.create("CREDIT_CARD", "4111111111111234");
        BookingReceipt receipt2 = facade.confirmBooking(booking2.getBookingId(), card);
        System.out.println(receipt2);

        System.out.println("=== Cancelling Booking: " + booking2.getBookingId() + " ===");
        facade.cancelBooking(booking2.getBookingId());
        System.out.println("  Status after cancel: " + facade.getBooking(booking2.getBookingId()).getStatus());

        // ── 10. Verify S2 and S4 are available again after cancellation ───────

        System.out.println("\n=== Available Seats after cancellation ===");
        facade.getAvailableSeats("SH1").forEach(s ->
            System.out.println("  " + s));

        // ── 11. Test unsupported payment mode ────────────────────────────────

        System.out.println("\n=== Testing unsupported payment mode ===");
        try {
            PaymentFactory.create("CRYPTO", "wallet123");
        } catch (Exception e) {
            System.out.println("  Expected error: " + e.getMessage());
        }

        lockManager.shutdown();
        System.out.println("\n=== Demo Complete ===");
    }
}
