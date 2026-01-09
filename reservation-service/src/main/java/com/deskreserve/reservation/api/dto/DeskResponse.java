\
package com.deskreserve.reservation.api.dto;

public record DeskResponse(
        Long id,
        String name,
        String location,
        boolean active
) {}
