\
package com.deskreserve.notification.api;

import com.deskreserve.common.events.ReservationEvent;
import com.deskreserve.notification.service.NotificationStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationStore store;

    public NotificationController(NotificationStore store) {
        this.store = store;
    }

    @GetMapping
    public List<ReservationEvent> latest(@RequestParam(name = "limit", defaultValue = "25") int limit) {
        int safe = Math.max(1, Math.min(limit, 100));
        return store.latest(safe);
    }
}
