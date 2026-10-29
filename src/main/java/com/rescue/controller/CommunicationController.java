package com.rescue.controller;

import com.rescue.model.ActivityFeed;
import com.rescue.model.ChannelType;
import com.rescue.model.Contact;
import com.rescue.model.Recipient;
import com.rescue.model.RecipientType;
import com.rescue.model.SentMessage;
import com.rescue.service.strategy.Notifier;
import com.rescue.view.CommunicationView;

import javax.swing.SwingUtilities;

/**
 * MVC: Controller for the Communication screen.
 * The view only reports clicks here; this class builds the recipient, asks the Notifier
 * to send, and pushes the results back into the view.
 */
public class CommunicationController {

    private final CommunicationView view;
    private final Notifier notifier;
    private final ActivityFeed feed;

    public CommunicationController(CommunicationView view, Notifier notifier, ActivityFeed feed) {
        this.view = view;
        this.notifier = notifier;
        this.feed = feed;

        this.view.getBackBtn().addActionListener(e -> this.view.dispose());
        view.setRecipientTypes(RecipientType.values());
        view.setChannels(notifier.availableChannels());
        view.addSendListener(e -> sendMessage());
        view.addRecipientTypeListener(e -> onRecipientTypeChanged());

        // Observers also send messages (thank-yous, alerts), so refresh whenever the feed changes.
        feed.addChangeListener(() -> SwingUtilities.invokeLater(this::refresh));

        onRecipientTypeChanged();
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
        try {
            RecipientType type = view.getRecipientType();
            Recipient recipient = (type == RecipientType.PUBLIC)
                    ? Contact.PUBLIC_AUDIENCE
                    : new Contact(view.getRecipientName(), view.getEmail(), view.getPhone());
            ChannelType chosen = view.getSelectedChannel();   // null means Auto

            SentMessage result = (chosen == null)
                    ? notifier.send(recipient, type, text)
                    : notifier.send(recipient, type, text, chosen);

            refresh();
            if (result.isSent()) {
                view.showMessage("Sent by " + result.getChannel().label() + " to " + result.getAddress() + ".");
                view.clearMessage();
            } else {
                view.showMessage("Not sent: " + result.getFailureReason());
            }
        } catch (IllegalArgumentException ex) {
            view.showMessage(ex.getMessage());
        }
    }

    private void onRecipientTypeChanged() {
        RecipientType type = view.getRecipientType();
        view.setAutoChannelHint(notifier.getDefaultChannel(type));
        view.setContactFieldsEnabled(type != RecipientType.PUBLIC);
    }

    private void refresh() {
        view.showHistory(notifier.history());
        view.showFeed(feed.entries());
    }
}
