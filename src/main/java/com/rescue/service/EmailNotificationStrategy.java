package com.rescue.service;

import com.rescue.model.Recipient;

public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(Recipient recipient, String message) {
        System.out.printf("[Email preview to %s] %s%n", recipient.getEmail(), message);
    }
}