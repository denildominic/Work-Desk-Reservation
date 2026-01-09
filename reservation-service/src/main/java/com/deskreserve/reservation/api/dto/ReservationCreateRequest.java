\
package com.deskreserve.reservation.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationCreateRequest(
        @NotNull Long deskId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}
