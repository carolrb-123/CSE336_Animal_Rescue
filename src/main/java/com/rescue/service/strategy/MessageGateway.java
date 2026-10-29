package com.rescue.service.strategy;

import com.rescue.model.ChannelType;

/**
 * The thing that actually transmits text. Phase 1 uses ConsoleGateway (simulated sending).
 * Phase 2 hook (Adapter): wrap a real email or SMS library in a class that implements this.
 */
public interface MessageGateway {

    void transmit(ChannelType channel, String address, String text);
}
