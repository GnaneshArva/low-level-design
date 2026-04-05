package moviebooking.service;

import moviebooking.exception.BookingException;
import moviebooking.exception.PaymentFailedException;
import moviebooking.exception.SeatNotAvailableException;
import moviebooking.model.*;
import moviebooking.strategy.payment.PaymentStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SRP: Manages the booking lifecycle.
 * DIP: Depends on SeatLockManager and PricingService abstractions injected at construction.
 *
 * Concurrency note: seat availability check + lock is synchronized on the Show's seatMap
 * to prevent double-booking in concurrent requests. In production, this would be a
 * distributed lock (e.g., Redis Redlock).
 */
public class BookingService {

    private final SeatLockManager seatLockManager;
    private final PricingService pricingService;

    // bookingId → Booking
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();

    public BookingService(SeatLockManager seatLockManager, PricingService pricingService) {
        this.seatLockManager = seatLockManager;
        this.pricingService  = pricingService;
    }

    /**
     * Step 1: Lock selected seats and create a PENDING booking.
     * Synchronized on show to prevent race conditions on the same show's seats.
     */
    public Booking initBooking(Show show, List<String> seatIds) {
        synchronized (show) {
            List<Seat> seatsToBook = resolveAndValidateSeats(show, seatIds);
            double total = pricingService.calculateTotal(seatsToBook, show);

            seatLockManager.lockSeats(show.getShowId(), seatsToBook);

            String bookingId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Booking booking = new Booking.Builder()
                .bookingId(bookingId)
                .show(show)
                .selectedSeats(seatsToBook)
                .totalAmount(total)
                .status(BookingStatus.PENDING)
                .build();

            bookings.put(bookingId, booking);
            System.out.println("[BOOKING] Seats locked. BookingId=" + bookingId + " Total=₹" + total);
            return booking;
        }
    }

    /**
     * Step 2: Process payment. On success → CONFIRMED. On failure → release seats.
     */
    public BookingReceipt confirmBooking(String bookingId, PaymentStrategy paymentStrategy,
                                         String theaterName) {
        Booking booking = getBooking(bookingId);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException("Booking is not in PENDING moviebooking.state: " + booking.getStatus());
        }

        boolean paid = paymentStrategy.processPayment(booking.getTotalAmount());

        if (!paid) {
            // Payment failed → release seats back to AVAILABLE
            seatLockManager.unlockSeats(booking.getShow().getShowId(), booking.getSelectedSeats());
            booking.setStatus(BookingStatus.PAYMENT_FAILED);
            throw new PaymentFailedException("Payment failed for booking: " + bookingId);
        }

        // Transition locked seats → BOOKED
        for (Seat seat : booking.getSelectedSeats()) {
            seat.getState().book();
        }
        seatLockManager.cancelTimers(booking.getShow().getShowId(), booking.getSelectedSeats());

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentMode(paymentStrategy.getPaymentMode());
        System.out.println("[BOOKING] Confirmed: " + bookingId);

        return BookingReceipt.from(booking, theaterName);
    }

    /**
     * Step 3 (optional): Cancel a CONFIRMED booking before the show starts.
     */
    public void cancelBooking(String bookingId) {
        Booking booking = getBooking(bookingId);

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BookingException("Only CONFIRMED bookings can be cancelled.");
        }

        if (booking.getShow().hasStarted()) {
            throw new BookingException("Cannot cancel after show has started.");
        }

        // Release booked seats back to AVAILABLE
        for (Seat seat : booking.getSelectedSeats()) {
            seat.getState().release();
        }

        booking.setStatus(BookingStatus.CANCELLED);
        processRefund(booking);
        System.out.println("[BOOKING] Cancelled: " + bookingId);
    }

    /**
     * Refund processing — in production: call payment gateway's refund API.
     */
    private void processRefund(Booking booking) {
        System.out.println("[REFUND] ₹" + String.format("%.2f", booking.getTotalAmount())
            + " refund initiated for booking: " + booking.getBookingId());
    }

    public Booking getBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) throw new BookingException("Booking not found: " + bookingId);
        return booking;
    }

    // ─── Private Helpers ────────────────────────────────────────────────────

    private List<Seat> resolveAndValidateSeats(Show show, List<String> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("At least one seat must be selected.");
        }

        List<Seat> seats = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = show.getSeat(seatId); // throws if not found
            if (!seat.isAvailable()) {
                throw new SeatNotAvailableException(
                    "Seat " + seatId + " is not available (moviebooking.state=" + seat.getState().getStateName() + ")");
            }
            seats.add(seat);
        }
        return seats;
    }
}
