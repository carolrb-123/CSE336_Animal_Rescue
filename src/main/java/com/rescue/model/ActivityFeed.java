package com.rescue.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// The dashboard's list of what just happened (newest first).
// The observers add lines here, and the Communication screen shows them.
public class ActivityFeed {

    private final List<String> entries = new ArrayList<>();

    public void add(String text) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM HH:mm"));
        entries.add(0, time + "   " + text);   // index 0 = put it at the top
    }

    public List<String> getEntries() {
        return entries;
    }
}
