package moviebooking.facade;

import moviebooking.model.Booking;
import moviebooking.model.BookingReceipt;
import moviebooking.model.Seat;
import moviebooking.model.Show;
import moviebooking.service.BookingService;
import moviebooking.service.ShowService;
import moviebooking.strategy.payment.PaymentStrategy;

import java.util.List;

/**
 * Facade Pattern: Single entry point for all client interactions.
 * Hides internal moviebooking.service wiring from callers.
 * Clients (controllers, CLI, tests) talk only to BookingFacade.
 *
 * ISP: Only exposes methods callers actually need.
 */
public class BookingFacade {

    private final ShowService showService;
    private final BookingService bookingService;
    private final String theaterName;

    public BookingFacade(ShowService showService, BookingService bookingService, String theaterName) {
        this.showService    = showService;
        this.bookingService = bookingService;
        this.theaterName    = theaterName;
    }

    /** Search shows by movie name. */
    public List<Show> searchShows(String movieName) {
        return showService.searchShows(movieName);
    }

    /** View available seats for a show. */
    public List<Seat> getAvailableSeats(String showId) {
        return showService.getAvailableSeats(showId);
    }

    /**
     * Lock seats and create a PENDING booking.
     * Returns a bookingId to use in subsequent steps.
     */
    public Booking initiateBooking(String showId, List<String> seatIds) {
        Show show = showService.getShow(showId);
        return bookingService.initBooking(show, seatIds);
    }

    /**
     * Pay and confirm. Returns a receipt on success.
     */
    public BookingReceipt confirmBooking(String bookingId, PaymentStrategy paymentStrategy) {
        return bookingService.confirmBooking(bookingId, paymentStrategy, theaterName);
    }

    /** Cancel a confirmed booking and trigger refund. */
    public void cancelBooking(String bookingId) {
        bookingService.cancelBooking(bookingId);
    }

    /** Retrieve booking details. */
    public Booking getBooking(String bookingId) {
        return bookingService.getBooking(bookingId);
    }
}
