package com.rescue.model;

import java.time.LocalDateTime;

// One line in the message log: who it was for, how it was sent, and if it worked.
public class SentMessage implements Entity {

    private int id;
    private final LocalDateTime sentAt;
    private final String recipientName;
    private final RecipientType recipientType;
    private final ChannelType channel;     // null if no channel could reach the person
    private final String address;          // email, phone or page (null if it failed)
    private final String text;             // what was sent (null if it failed)
    private final boolean sent;            // true = sent, false = failed
    private final String failureReason;    // why it failed (null if it was sent)

    public SentMessage(String recipientName, RecipientType recipientType, ChannelType channel,
                       String address, String text, boolean sent, String failureReason) {
        this.sentAt = LocalDateTime.now();
        this.recipientName = recipientName;
        this.recipientType = recipientType;
        this.channel = channel;
        this.address = address;
        this.text = text;
        this.sent = sent;
        this.failureReason = failureReason;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public RecipientType getRecipientType() {
        return recipientType;
    }

    public ChannelType getChannel() {
        return channel;
    }

    public String getAddress() {
        return address;
    }

    public String getText() {
        return text;
    }

    public boolean isSent() {
        return sent;
    }

    public String getFailureReason() {
        return failureReason;
    }

    @Override
    public String toString() {
        if (sent) {
            return "[" + channel + " -> " + recipientName + "] " + text;
        }
        return "[FAILED -> " + recipientName + "] " + failureReason;
    }
}
