package com.notification;

import com.notification.facade.NotificationFacade;
import com.notification.model.*;

import java.util.Arrays;
import java.util.Map;

/**
 * Demo runner: exercises all major system features.
 *
 * Demonstrates:
 *   1. Simple OTP via email with SMS fallback
 *   2. Order confirmation with priority
 *   3. Push promo (no fallback, low priority)
 *   4. Priority ordering across a batch
 *   5. Full-control Notification.builder() usage
 *   6. Custom template registration
 *   7. Delivery status query
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        NotificationFacade facade = new NotificationFacade();

        // ── Scenario 1: OTP via Email (CRITICAL priority, SMS fallback) ────────
        printSection("SCENARIO 1: OTP Notification (Critical Priority + Fallback)");

        Recipient alice = Recipient.builder("Alice")
                .email("alice@example.com")
                .phoneNumber("+91-9876543210")
                .deviceToken("device-token-alice-001")
                .build();

        // Note: EmailChannel has a 30% simulated failure rate — retry + fallback may activate
        facade.sendOtp(alice, "847291", ChannelType.EMAIL);

        Thread.sleep(200);

        // ── Scenario 2: Order Confirmation (HIGH priority) ─────────────────────
        printSection("SCENARIO 2: Order Confirmation (High Priority)");

        Recipient bob = Recipient.builder("Bob")
                .email("bob@example.com")
                .phoneNumber("+91-9123456789")
                .build();

        facade.sendOrderConfirmation(bob, "ORD-20240105", "Jan 08, 2026");

        Thread.sleep(200);

        // ── Scenario 3: Promo Push (LOW priority, no fallback) ────────────────
        printSection("SCENARIO 3: Promotional Push (Low Priority, No Fallback)");

        Recipient carol = Recipient.builder("Carol")
                .deviceToken("device-token-carol-xyz")
                .build();

        facade.sendPromo(carol, "20", "SAVE20NOW");

        Thread.sleep(200);

        // ── Scenario 4: Priority Batch — demonstrates queue ordering ──────────
        printSection("SCENARIO 4: Priority Batch (CRITICAL > HIGH > LOW processed first)");

        Recipient dave = Recipient.builder("Dave")
                .email("dave@example.com")
                .phoneNumber("+91-9000000001")
                .deviceToken("device-dave-token")
                .build();

        Notification lowPriorityPromo = Notification.builder()
                .recipient(dave)
                .subject("Weekend Sale!")
                .templateKey("PROMO")
                .templateData(Map.of("name", "Dave", "discount", "15", "code", "WEEKEND15"))
                .priority(Priority.LOW)
                .channel(ChannelType.PUSH)
                .build();

        Notification criticalAlert = Notification.builder()
                .recipient(dave)
                .subject("Security Alert")
                .templateKey("ALERT")
                .templateData(Map.of("message", "Suspicious login detected on your account"))
                .priority(Priority.CRITICAL)
                .channel(ChannelType.SMS)
                .build();

        Notification highOrderConf = Notification.builder()
                .recipient(dave)
                .subject("Order Confirmed")
                .templateKey("ORDER_CONFIRMATION")
                .templateData(Map.of("name", "Dave", "orderId", "ORD-99981", "deliveryDate", "Jan 10, 2026"))
                .priority(Priority.HIGH)
                .channel(ChannelType.EMAIL)
                .fallback(ChannelType.SMS)
                .build();

        // Deliberately enqueued LOW → HIGH → CRITICAL; expect processing CRITICAL → HIGH → LOW
        System.out.println("Enqueuing: LOW promo, HIGH order, CRITICAL alert — expect CRITICAL first");
        facade.sendAll(Arrays.asList(lowPriorityPromo, highOrderConf, criticalAlert));

        Thread.sleep(200);

        // ── Scenario 5: Custom template registration ──────────────────────────
        printSection("SCENARIO 5: Custom Template + SMS Channel");

        facade.registerTemplate("APPOINTMENT",
                "Hi {{name}}, your appointment is confirmed for {{date}} at {{time}}.");

        Recipient eve = Recipient.builder("Eve")
                .phoneNumber("+91-9555555555")
                .build();

        Notification appointment = Notification.builder()
                .recipient(eve)
                .subject("Appointment Confirmed")
                .templateKey("APPOINTMENT")
                .templateData(Map.of("name", "Eve", "date", "Jan 12, 2026", "time", "10:30 AM"))
                .priority(Priority.HIGH)
                .channel(ChannelType.SMS)
                .build();

        facade.send(appointment);

        System.out.printf("%n[STATUS] Appointment notification status: %s%n",
                facade.getStatus(appointment.getId()));

        Thread.sleep(200);

        // ── Scenario 6: Invalid channel (no device token) → ChannelDelivery failure ──
        printSection("SCENARIO 6: Missing Contact Info → Graceful Failure");

        Recipient frank = Recipient.builder("Frank")
                .email("frank@example.com")
                // Intentionally no phoneNumber — SMS will fail
                .build();

        Notification smsToFrank = Notification.builder()
                .recipient(frank)
                .subject("Test")
                .templateKey("WELCOME")
                .templateData(Map.of("name", "Frank"))
                .priority(Priority.MEDIUM)
                .channel(ChannelType.SMS)   // Will fail — no phone number
                // No fallback configured
                .build();

        facade.send(smsToFrank);
        System.out.printf("[STATUS] Frank's notification status: %s%n",
                facade.getStatus(smsToFrank.getId()));

        printSection("DEMO COMPLETE");
    }

    private static void printSection(String title) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("  " + title);
        System.out.println("─".repeat(60));
    }
}
