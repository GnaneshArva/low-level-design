package moviebooking.service;

import moviebooking.model.Seat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * SRP: Owns the lock-expiry lifecycle only.
 * Uses ScheduledExecutorService to release locked seats after timeout.
 *
 * Trade-off: In a distributed system, this would be a Redis TTL lock.
 * Here we simulate it with an in-memory scheduler for interview clarity.
 */
public class SeatLockManager {

    private static final long LOCK_TIMEOUT_SECONDS = 300; // 5 minutes

    // lockKey (showId+seatId) → scheduled future for cancellation
    private final Map<String, ScheduledFuture<?>> lockTimers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler =
        Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "seat-lock-expiry");
            t.setDaemon(true); // don't block JVM shutdown
            return t;
        });

    /**
     * Lock seats and schedule auto-release on timeout.
     */
    public void lockSeats(String showId, List<Seat> seats) {
        for (Seat seat : seats) {
            seat.getState().lock();
            scheduleRelease(showId, seat);
        }
    }

    /**
     * Cancel the timer and release locks explicitly (on payment success or failure).
     */
    public void unlockSeats(String showId, List<Seat> seats) {
        for (Seat seat : seats) {
            cancelTimer(showId, seat.getSeatId());
            seat.getState().release();
        }
    }

    /**
     * Cancel timers when booking is confirmed — seats are now BOOKED, not locked.
     */
    public void cancelTimers(String showId, List<Seat> seats) {
        for (Seat seat : seats) {
            cancelTimer(showId, seat.getSeatId());
        }
    }

    private void scheduleRelease(String showId, Seat seat) {
        String key = lockKey(showId, seat.getSeatId());
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            if (seat.getState().isLocked()) {
                seat.getState().release();
                System.out.println("[TIMEOUT] Seat auto-released: " + seat.getSeatId());
            }
            lockTimers.remove(key);
        }, LOCK_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        lockTimers.put(key, future);
    }

    private void cancelTimer(String showId, String seatId) {
        String key = lockKey(showId, seatId);
        ScheduledFuture<?> future = lockTimers.remove(key);
        if (future != null) future.cancel(false);
    }

    private String lockKey(String showId, String seatId) {
        return showId + ":" + seatId;
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}
