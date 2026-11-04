package com.rescue.controller;

import com.rescue.model.ActivityFeed;
import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Recipient;
import com.rescue.model.RecipientType;
import com.rescue.model.SentMessage;
import com.rescue.service.strategy.Notifier;
import com.rescue.view.CommunicationView;

// MVC - the Controller of the Communication screen.
// It reads the form, asks the Notifier to send, and updates the screen.
public class CommunicationController {

    private final CommunicationView view;
    private final Notifier notifier;
    private final ActivityFeed feed;

    public CommunicationController(CommunicationView view, Notifier notifier, ActivityFeed feed) {
        this.view = view;
        this.notifier = notifier;
        this.feed = feed;

        view.addSendListener(e -> sendMessage());
        view.addRefreshListener(e -> refresh());
        view.addRecipientTypeListener(e -> recipientTypeChanged());
        view.getBackBtn().addActionListener(e -> view.dispose());

        recipientTypeChanged();
        refresh();
    }

    public CommunicationView getView() {
        return view;
    }

    private void sendMessage() {
        String text = view.getMessageText();
        if (text.isEmpty()) {
            view.showMessage("Please write a message.");
            return;
        }

        RecipientType type = view.getRecipientType();
        Recipient recipient;
        if (type == RecipientType.PUBLIC) {
            recipient = Contact.PUBLIC_AUDIENCE;
        } else {
            if (view.getRecipientName().isEmpty()) {
                view.showMessage("Please enter the recipient's name.");
                return;
            }
            recipient = new Contact(view.getRecipientName(), view.getEmail(), view.getPhone());
        }

        SentMessage result;
        String channel = view.getSelectedChannel();
        if (channel.equals("AUTO")) {
            result = notifier.send(recipient, type, text);
        } else {
            result = notifier.send(recipient, type, text, ChannelType.valueOf(channel));
        }

        refresh();
        if (result.isSent()) {
            view.showMessage("Sent by " + result.getChannel() + " to " + result.getAddress() + ".");
            view.clearMessage();
        } else {
            view.showMessage("Not sent: " + result.getFailureReason());
        }
    }

    // When the recipient type changes, show which channel "AUTO" will use.
    private void recipientTypeChanged() {
        RecipientType type = view.getRecipientType();
        view.setAutoHint("AUTO sends to a " + type + " by " + notifier.getDefaultChannel(type));
        // A public post has no name, email or phone.
        view.setContactFieldsEnabled(type != RecipientType.PUBLIC);
    }

    private void refresh() {
        view.showHistory(notifier.getHistory());
        view.showFeed(feed.getEntries());
    }
}
