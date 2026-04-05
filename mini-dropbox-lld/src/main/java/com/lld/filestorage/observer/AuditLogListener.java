package com.lld.filestorage.observer;

/**
 * Concrete observer: writes a structured audit log on every file system event.
 * In production this would write to a SIEM or append-only audit table.
 *
 * SRP: Only audit logging — no business logic.
 * OCP: New listeners (EmailNotifier, MetricsListener) are added independently.
 */
public final class AuditLogListener implements FileEventListener {

    @Override
    public void onEvent(final FileEvent event) {
        System.out.printf("[AUDIT] %-12s | path=%-30s | actor=%s | at=%s%n",
                event.getEventType(),
                event.getNodePath(),
                event.getActorUserId(),
                event.getOccurredAt());
    }
}
