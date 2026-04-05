package moviebooking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Immutable receipt produced after a confirmed booking.
 * Builder Pattern gives a readable, safe construction path.
 */
public final class BookingReceipt {

    private final String bookingId;
    private final String movieName;
    private final String theaterName;
    private final LocalDateTime showTime;
    private final List<String> seatDescriptions;
    private final double totalAmount;
    private final String paymentMode;
    private final LocalDateTime issuedAt;

    private BookingReceipt(Builder builder) {
        this.bookingId        = builder.bookingId;
        this.movieName        = builder.movieName;
        this.theaterName      = builder.theaterName;
        this.showTime         = builder.showTime;
        this.seatDescriptions = List.copyOf(builder.seatDescriptions);
        this.totalAmount      = builder.totalAmount;
        this.paymentMode      = builder.paymentMode;
        this.issuedAt         = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        return "\n========== BOOKING RECEIPT ==========\n" +
            "Booking ID   : " + bookingId + "\n" +
            "Movie        : " + movieName + "\n" +
            "Theater      : " + theaterName + "\n" +
            "Show Time    : " + showTime.format(fmt) + "\n" +
            "Seats        : " + String.join(", ", seatDescriptions) + "\n" +
            "Total Amount : ₹" + String.format("%.2f", totalAmount) + "\n" +
            "Payment Mode : " + paymentMode + "\n" +
            "Issued At    : " + issuedAt.format(fmt) + "\n" +
            "=====================================\n";
    }

    // ─── Builder ────────────────────────────────────────────────────────────

    public static class Builder {
        private String bookingId;
        private String movieName;
        private String theaterName;
        private LocalDateTime showTime;
        private List<String> seatDescriptions;
        private double totalAmount;
        private String paymentMode;

        public Builder bookingId(String id)              { this.bookingId = id; return this; }
        public Builder movieName(String name)            { this.movieName = name; return this; }
        public Builder theaterName(String name)          { this.theaterName = name; return this; }
        public Builder showTime(LocalDateTime time)      { this.showTime = time; return this; }
        public Builder seatDescriptions(List<String> s)  { this.seatDescriptions = s; return this; }
        public Builder totalAmount(double amt)           { this.totalAmount = amt; return this; }
        public Builder paymentMode(String mode)          { this.paymentMode = mode; return this; }

        public BookingReceipt build() {
            return new BookingReceipt(this);
        }
    }

    // Static convenience to build from a confirmed Booking
    public static BookingReceipt from(Booking booking, String theaterName) {
        List<String> seatDesc = booking.getSelectedSeats().stream()
            .map(s -> s.getRow() + s.getNumber() + "(" + s.getSeatType() + ")")
            .collect(Collectors.toList());

        return new Builder()
            .bookingId(booking.getBookingId())
            .movieName(booking.getShow().getMovie().getName())
            .theaterName(theaterName)
            .showTime(booking.getShow().getStartTime())
            .seatDescriptions(seatDesc)
            .totalAmount(booking.getTotalAmount())
            .paymentMode(booking.getPaymentMode())
            .build();
    }
}
