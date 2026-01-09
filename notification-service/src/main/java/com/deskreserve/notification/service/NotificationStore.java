\
package com.deskreserve.notification.service;

import com.deskreserve.common.events.ReservationEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class NotificationStore {

    private final Deque<ReservationEvent> events = new ConcurrentLinkedDeque<>();
    private final int maxSize = 100;

    public void add(ReservationEvent event) {
        events.addFirst(event);
        while (events.size() > maxSize) {
            events.removeLast();
        }
    }

    public List<ReservationEvent> latest(int limit) {
        List<ReservationEvent> out = new ArrayList<>(Math.min(limit, events.size()));
        int i = 0;
        for (ReservationEvent e : events) {
            out.add(e);
            i++;
            if (i >= limit) break;
        }
        return out;
    }
}
