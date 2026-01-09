\
package com.deskreserve.reservation.service;

import com.deskreserve.common.events.ReservationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationEventPublisher {

    private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;
    private final String topic;

    public ReservationEventPublisher(
            KafkaTemplate<String, ReservationEvent> kafkaTemplate,
            @Value("${app.kafka.topics.reservations}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(ReservationEvent event) {
        String key = event.reservationId() != null ? event.reservationId().toString() : UUID.randomUUID().toString();
        kafkaTemplate.send(topic, key, event);
    }
}
