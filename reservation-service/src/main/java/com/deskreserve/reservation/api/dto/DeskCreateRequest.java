\
package com.deskreserve.reservation.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeskCreateRequest(
        @NotBlank @Size(max = 80) String name,
        @NotBlank @Size(max = 120) String location
) {}
