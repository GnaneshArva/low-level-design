package com.lld.filestorage.observer;

import java.time.Instant;

/**
 * Immutable event published to listeners when a file system operation occurs.
 * OCP: New event types (RENAME, RESTORE_VERSION) are added as new EventType enum values.
 */
public final class FileEvent {

    public enum EventType { UPLOADED, DOWNLOADED, DELETED, SHARED }

    private final EventType eventType;
    private final String    nodePath;     // logical path of affected node
    private final String    actorUserId;  // user who triggered the event
    private final Instant   occurredAt;

    public FileEvent(final EventType eventType, final String nodePath, final String actorUserId) {
        if (eventType   == null) throw new IllegalArgumentException("eventType required");
        if (nodePath    == null) throw new IllegalArgumentException("nodePath required");
        if (actorUserId == null) throw new IllegalArgumentException("actorUserId required");
        this.eventType   = eventType;
        this.nodePath    = nodePath;
        this.actorUserId = actorUserId;
        this.occurredAt  = Instant.now();
    }

    public EventType getEventType()   { return eventType; }
    public String    getNodePath()    { return nodePath; }
    public String    getActorUserId() { return actorUserId; }
    public Instant   getOccurredAt()  { return occurredAt; }

    @Override
    public String toString() {
        return "FileEvent{" + eventType + " on '" + nodePath + "' by " + actorUserId + " at " + occurredAt + "}";
    }
}
