\
package com.deskreserve.notification.service;

import com.deskreserve.common.events.ReservationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventListener {

    private static final Logger log = LoggerFactory.getLogger(ReservationEventListener.class);

    private final NotificationStore store;

    public ReservationEventListener(NotificationStore store) {
        this.store = store;
    }

    @KafkaListener(topics = "${app.kafka.topics.reservations}")
    public void onEvent(ReservationEvent event) {
        log.info("Received event: type={}, reservationId={}, deskId={}, username={}, occurredAt={}",
                event.type(), event.reservationId(), event.deskId(), event.username(), event.occurredAt());
        store.add(event);
    }
}
