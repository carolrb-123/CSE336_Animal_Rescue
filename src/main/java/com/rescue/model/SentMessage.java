package com.rescue.model;

import java.time.LocalDateTime;

/** One entry in the message log: what was sent, to whom, by which channel, and whether it worked. */
public class SentMessage implements Entity {

    public enum Status { SENT, FAILED }

    private int id;
    private final LocalDateTime sentAt = LocalDateTime.now();
    private final String recipientName;
    private final RecipientType recipientType;
    private final ChannelType channel;      // null when no channel could be chosen
    private final String address;           // email, phone or page; null when it failed
    private final String text;              // exactly what went out; null when it failed
    private final Status status;
    private final String failureReason;     // null when sent

    private SentMessage(Recipient recipient, RecipientType type, ChannelType channel, String address,
                        String text, Status status, String failureReason) {
        this.recipientName = recipient.getName();
        this.recipientType = type;
        this.channel = channel;
        this.address = address;
        this.text = text;
        this.status = status;
        this.failureReason = failureReason;
    }

    public static SentMessage sent(Recipient recipient, RecipientType type, ChannelType channel,
                                   String address, String text) {
        return new SentMessage(recipient, type, channel, address, text, Status.SENT, null);
    }

    public static SentMessage failed(Recipient recipient, RecipientType type, ChannelType channel, String reason) {
        return new SentMessage(recipient, type, channel, null, null, Status.FAILED, reason);
    }

    @Override
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getSentAt() { return sentAt; }
    public String getRecipientName() { return recipientName; }
    public RecipientType getRecipientType() { return recipientType; }
    public ChannelType getChannel() { return channel; }
    public String getAddress() { return address; }
    public String getText() { return text; }
    public Status getStatus() { return status; }
    public String getFailureReason() { return failureReason; }

    public boolean isSent() {
        return status == Status.SENT;
    }

    @Override
    public String toString() {
        return isSent()
                ? "[" + channel + " -> " + recipientName + " <" + address + ">] " + text.replace('\n', ' ')
                : "[FAILED -> " + recipientName + "] " + failureReason;
    }
}
