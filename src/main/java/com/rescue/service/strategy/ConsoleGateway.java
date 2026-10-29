package com.rescue.service.strategy;

import com.rescue.model.ChannelType;

/** Simulated sending for Phase 1: prints what would go out. */
public class ConsoleGateway implements MessageGateway {

    @Override
    public void transmit(ChannelType channel, String address, String text) {
        System.out.println("----- " + channel.label() + " to " + address + " -----");
        System.out.println(text);
        System.out.println();
    }
}
