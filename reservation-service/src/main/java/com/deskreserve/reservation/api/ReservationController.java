\
package com.deskreserve.reservation.api;

import com.deskreserve.reservation.api.dto.ReservationCreateRequest;
import com.deskreserve.reservation.api.dto.ReservationResponse;
import com.deskreserve.reservation.domain.Employee;
import com.deskreserve.reservation.domain.Reservation;
import com.deskreserve.reservation.repo.EmployeeRepository;
import com.deskreserve.reservation.security.SecurityUtil;
import com.deskreserve.reservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final EmployeeRepository employeeRepository;

    public ReservationController(ReservationService reservationService, EmployeeRepository employeeRepository) {
        this.reservationService = reservationService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody ReservationCreateRequest req, Authentication auth) {
        String username = SecurityUtil.username(auth);
        Reservation r = reservationService.createReservation(username, req.deskId(), req.startDate(), req.endDate());
        return toDto(r);
    }

    @GetMapping("/my")
    public List<ReservationResponse> myReservations(Authentication auth) {
        String username = SecurityUtil.username(auth);
        Employee emp = employeeRepository.findByUsername(username)
                .orElseGet(() -> employeeRepository.save(new Employee(username, username)));
        return reservationService.listForUser(emp).stream().map(ReservationController::toDto).toList();
    }

    @DeleteMapping("/{id}")
    public ReservationResponse cancel(@PathVariable("id") UUID id, Authentication auth) {
        String username = SecurityUtil.username(auth);
        boolean isAdmin = SecurityUtil.isAdmin(auth);
        Reservation r = reservationService.cancel(id, username, isAdmin);
        return toDto(r);
    }

    private static ReservationResponse toDto(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getDesk().getId(),
                r.getDesk().getName(),
                r.getStartDate(),
                r.getEndDate(),
                r.getStatus()
        );
    }
}
