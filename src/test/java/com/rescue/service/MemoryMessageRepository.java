package com.rescue.service;

import com.rescue.model.SentMessage;
import com.rescue.repo.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// A fresh, empty message log for each test (the real one is shared in the DataStore).
public class MemoryMessageRepository implements Repository<SentMessage> {

    private final List<SentMessage> messages = new ArrayList<>();

    @Override
    public void save(SentMessage message) {
        message.setId(messages.size() + 1);
        messages.add(message);
    }

    @Override
    public Optional<SentMessage> findById(int id) {
        for (SentMessage m : messages) {
            if (m.getId() == id) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<SentMessage> findAll() {
        return new ArrayList<>(messages);
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == id) {
                messages.remove(i);
                return;
            }
        }
    }

    // How many messages were SENT on this channel.
    public int countSent(com.rescue.model.ChannelType channel) {
        int count = 0;
        for (SentMessage m : messages) {
            if (m.isSent() && m.getChannel() == channel) {
                count++;
            }
        }
        return count;
    }
}
