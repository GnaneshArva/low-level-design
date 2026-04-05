package moviebooking.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Booking is built via Builder to handle optional fields cleanly
 * and produce a readable construction call site.
 * Once confirmed, Booking is effectively immutable (status aside).
 */
public class Booking {

    private final String bookingId;
    private final Show show;
    private final List<Seat> selectedSeats;
    private final double totalAmount;
    private final LocalDateTime bookingTime;
    private String paymentMode;

    // Mutable: status transitions across the booking lifecycle
    private BookingStatus status;

    private Booking(Builder builder) {
        this.bookingId     = builder.bookingId;
        this.show          = builder.show;
        this.selectedSeats = Collections.unmodifiableList(builder.selectedSeats);
        this.totalAmount   = builder.totalAmount;
        this.bookingTime   = builder.bookingTime;
        this.paymentMode   = builder.paymentMode;
        this.status        = builder.status;
    }

    public String getBookingId()           { return bookingId; }
    public Show getShow()                  { return show; }
    public List<Seat> getSelectedSeats()   { return selectedSeats; }
    public double getTotalAmount()         { return totalAmount; }
    public LocalDateTime getBookingTime()  { return bookingTime; }
    public String getPaymentMode()         { return paymentMode; }
    public BookingStatus getStatus()       { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }
    public void setPaymentMode(String mode)      { this.paymentMode = mode; }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + bookingId +
            ", show=" + show.getMovie().getName() +
            ", seats=" + selectedSeats.size() +
            ", amount=₹" + totalAmount +
            ", status=" + status +
            "}";
    }

    // ─── Builder ────────────────────────────────────────────────────────────

    public static class Builder {
        private String bookingId;
        private Show show;
        private List<Seat> selectedSeats;
        private double totalAmount;
        private LocalDateTime bookingTime = LocalDateTime.now();
        private String paymentMode;
        private BookingStatus status = BookingStatus.PENDING;

        public Builder bookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder show(Show show) {
            this.show = show;
            return this;
        }

        public Builder selectedSeats(List<Seat> seats) {
            this.selectedSeats = seats;
            return this;
        }

        public Builder totalAmount(double amount) {
            this.totalAmount = amount;
            return this;
        }

        public Builder bookingTime(LocalDateTime time) {
            this.bookingTime = time;
            return this;
        }

        public Builder paymentMode(String mode) {
            this.paymentMode = mode;
            return this;
        }

        public Builder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        public Booking build() {
            if (bookingId == null || show == null || selectedSeats == null || selectedSeats.isEmpty()) {
                throw new IllegalStateException("bookingId, show, and selectedSeats are required");
            }
            return new Booking(this);
        }
    }
}
