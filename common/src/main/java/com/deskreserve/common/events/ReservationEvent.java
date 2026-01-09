package com.deskreserve.common.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Shared Kafka event DTO.
 */
public record ReservationEvent(
        String type,
        UUID reservationId,
        Long deskId,
        String username,
        Instant occurredAt
) {}
