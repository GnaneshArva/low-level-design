package com.notification.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Core domain object representing a notification request.
 *
 * Design decisions:
 * - Immutable after construction (all fields final, no setters)
 * - Builder pattern handles complex construction and validation at boundaries
 * - DeliveryStatus is mutable via a controlled mutator — the only intentional
 *   exception to immutability, since status reflects runtime state.
 * - templateKey is separate from renderedMessage: the engine resolves it before sending.
 */
public final class Notification {

    private final String id;
    private final Recipient recipient;
    private final String subject;           // Used by email channel
    private final String templateKey;       // Key to look up in TemplateEngine
    private final Object templateData;      // Data to bind into template
    private final Priority priority;
    private final ChannelType primaryChannel;
    private final ChannelType fallbackChannel; // nullable
    private final Instant createdAt;

    // Mutable runtime state — not part of the value identity
    private volatile DeliveryStatus status;
    private volatile String renderedMessage;    // Set after template is applied
    private volatile int attemptCount;

    private Notification(Builder builder) {
        this.id              = UUID.randomUUID().toString();
        this.recipient       = builder.recipient;
        this.subject         = builder.subject;
        this.templateKey     = builder.templateKey;
        this.templateData    = builder.templateData;
        this.priority        = builder.priority;
        this.primaryChannel  = builder.primaryChannel;
        this.fallbackChannel = builder.fallbackChannel;
        this.createdAt       = Instant.now();
        this.status          = DeliveryStatus.PENDING;
        this.attemptCount    = 0;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public String getId()                         { return id; }
    public Recipient getRecipient()               { return recipient; }
    public String getSubject()                    { return subject; }
    public String getTemplateKey()                { return templateKey; }
    public Object getTemplateData()               { return templateData; }
    public Priority getPriority()                 { return priority; }
    public ChannelType getPrimaryChannel()        { return primaryChannel; }
    public ChannelType getFallbackChannel()       { return fallbackChannel; }
    public Instant getCreatedAt()                 { return createdAt; }
    public DeliveryStatus getStatus()             { return status; }
    public String getRenderedMessage()            { return renderedMessage; }
    public int getAttemptCount()                  { return attemptCount; }
    public boolean hasFallback()                  { return fallbackChannel != null; }

    // ── Controlled mutators (runtime state only) ──────────────────────────────

    public void setStatus(DeliveryStatus status)          { this.status = status; }
    public void setRenderedMessage(String message)        { this.renderedMessage = message; }
    public void incrementAttempt()                        { this.attemptCount++; }

    @Override
    public String toString() {
        return String.format("Notification{id='%s', recipient=%s, channel=%s, priority=%s, status=%s}",
                id, recipient, primaryChannel, priority, status);
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Recipient recipient;
        private String subject = "";
        private String templateKey;
        private Object templateData;
        private Priority priority = Priority.MEDIUM;
        private ChannelType primaryChannel;
        private ChannelType fallbackChannel;

        private Builder() {}

        public Builder recipient(Recipient recipient)         { this.recipient = recipient; return this; }
        public Builder subject(String subject)                { this.subject = subject; return this; }
        public Builder templateKey(String templateKey)        { this.templateKey = templateKey; return this; }
        public Builder templateData(Object templateData)      { this.templateData = templateData; return this; }
        public Builder priority(Priority priority)            { this.priority = priority; return this; }
        public Builder channel(ChannelType channel)           { this.primaryChannel = channel; return this; }
        public Builder fallback(ChannelType fallback)         { this.fallbackChannel = fallback; return this; }

        public Notification build() {
            if (recipient == null)      throw new IllegalArgumentException("Recipient is required");
            if (primaryChannel == null) throw new IllegalArgumentException("Primary channel is required");
            if (templateKey == null || templateKey.isBlank())
                                        throw new IllegalArgumentException("Template key is required");
            return new Notification(this);
        }
    }
}
