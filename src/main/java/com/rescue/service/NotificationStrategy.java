package com.rescue.service;

import com.rescue.model.Recipient;

public interface NotificationStrategy {
    void send(Recipient r, String message);
}
