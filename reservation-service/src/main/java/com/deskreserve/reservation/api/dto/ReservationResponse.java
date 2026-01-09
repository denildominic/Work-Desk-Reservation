\
package com.deskreserve.reservation.api.dto;

import com.deskreserve.reservation.domain.ReservationStatus;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        Long deskId,
        String deskName,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status
) {}
