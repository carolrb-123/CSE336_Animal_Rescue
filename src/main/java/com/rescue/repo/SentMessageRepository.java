package com.rescue.repo;

import com.rescue.model.DataStore;
import com.rescue.model.SentMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Stores every message we sent (or tried to send) in the shared DataStore.
public class SentMessageRepository implements Repository<SentMessage> {

    private final Map<Integer, SentMessage> messages = DataStore.getInstance().getSentMessages();

    @Override
    public void save(SentMessage message) {
        if (message.getId() <= 0) {
            // Give it the next free number, so an old message is never overwritten.
            int biggestId = 0;
            for (int id : messages.keySet()) {
                if (id > biggestId) {
                    biggestId = id;
                }
            }
            message.setId(biggestId + 1);
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Optional<SentMessage> findById(int id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<SentMessage> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public void delete(int id) {
        messages.remove(id);
    }
}
