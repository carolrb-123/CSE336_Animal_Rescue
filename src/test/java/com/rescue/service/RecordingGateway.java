package com.rescue.service;

import com.rescue.model.ChannelType;
import com.rescue.service.strategy.MessageGateway;

import java.util.ArrayList;
import java.util.List;

/** Test double: remembers what was transmitted instead of printing it, and can be told to fail. */
public class RecordingGateway implements MessageGateway {

    public record Sent(ChannelType channel, String address, String text) { }

    public final List<Sent> sent = new ArrayList<>();
    public boolean failNext = false;

    @Override
    public void transmit(ChannelType channel, String address, String text) {
        if (failNext) {
            failNext = false;
            throw new IllegalStateException("gateway down");
        }
        sent.add(new Sent(channel, address, text));
    }

    public long count(ChannelType channel) {
        return sent.stream().filter(s -> s.channel() == channel).count();
    }
}
