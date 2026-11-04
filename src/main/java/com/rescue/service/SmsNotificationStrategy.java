package com.rescue.service;

import com.rescue.model.Recipient;

public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(Recipient recipient, String message) {
        System.out.printf("[SMS preview to %s] %s%n", recipient.getPhone(), message);
    }
}