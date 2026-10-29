package com.rescue.repo;

import com.rescue.model.DataStore;
import com.rescue.model.SentMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class SentMessageRepository implements Repository<SentMessage> {


    private final Map<Integer, SentMessage> messages = DataStore.getInstance().getSentMessages();
    private int nextId = 1;

    @Override
    public synchronized void save(SentMessage message) {
        if (message.getId() <= 0) {
            message.setId(nextId++);
        } else {
            nextId = Math.max(nextId, message.getId() + 1);
        }
        messages.put(message.getId(), message);
    }

    @Override
    public synchronized Optional<SentMessage> findById(int id) {
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public synchronized List<SentMessage> findAll() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public synchronized void delete(int id) {
        messages.remove(id);
    }
}