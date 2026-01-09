\
package com.deskreserve.reservation.api;

import com.deskreserve.reservation.api.dto.DeskCreateRequest;
import com.deskreserve.reservation.api.dto.DeskResponse;
import com.deskreserve.reservation.domain.Desk;
import com.deskreserve.reservation.service.DeskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desks")
public class DeskController {

    private final DeskService deskService;

    public DeskController(DeskService deskService) {
        this.deskService = deskService;
    }

    @GetMapping
    public List<DeskResponse> list() {
        return deskService.list().stream().map(DeskController::toDto).toList();
    }

    @PostMapping
    public DeskResponse create(@Valid @RequestBody DeskCreateRequest req) {
        Desk desk = deskService.create(req.name(), req.location());
        return toDto(desk);
    }

    private static DeskResponse toDto(Desk d) {
        return new DeskResponse(d.getId(), d.getName(), d.getLocation(), d.isActive());
    }
}
