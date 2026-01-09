\
package com.deskreserve.reservation.service;

import com.deskreserve.common.events.ReservationEvent;
import com.deskreserve.reservation.api.ApiException;
import com.deskreserve.reservation.domain.*;
import com.deskreserve.reservation.repo.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DeskService deskService;
    private final EmployeeService employeeService;
    private final ReservationEventPublisher eventPublisher;

    public ReservationService(ReservationRepository reservationRepository,
                              DeskService deskService,
                              EmployeeService employeeService,
                              ReservationEventPublisher eventPublisher) {
        this.reservationRepository = reservationRepository;
        this.deskService = deskService;
        this.employeeService = employeeService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Reservation createReservation(String username, Long deskId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "startDate must be <= endDate");
        }
        Desk desk = deskService.getRequired(deskId);
        if (!desk.isActive()) {
            throw new ApiException(HttpStatus.CONFLICT, "Desk is not active");
        }

        boolean overlapping = reservationRepository.existsOverlapping(
                deskId, ReservationStatus.ACTIVE, startDate, endDate
        );
        if (overlapping) {
            throw new ApiException(HttpStatus.CONFLICT, "Desk already reserved for the requested dates");
        }

        Employee employee = employeeService.getOrCreate(username);
        Reservation reservation = new Reservation(UUID.randomUUID(), desk, employee, startDate, endDate);
        Reservation saved = reservationRepository.save(reservation);

        eventPublisher.publish(new ReservationEvent(
                "RESERVATION_CREATED",
                saved.getId(),
                desk.getId(),
                employee.getUsername(),
                Instant.now()
        ));

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Reservation> listForUser(Employee employee) {
        return reservationRepository.findByEmployeeOrderByStartDateDesc(employee);
    }

    @Transactional
    public Reservation cancel(UUID reservationId, String username, boolean isAdmin) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Reservation not found"));

        if (!isAdmin && !reservation.getEmployee().getUsername().equals(username)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You can only cancel your own reservations");
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            return reservation;
        }

        reservation.cancel();
        Reservation saved = reservationRepository.save(reservation);

        eventPublisher.publish(new ReservationEvent(
                "RESERVATION_CANCELLED",
                saved.getId(),
                saved.getDesk().getId(),
                saved.getEmployee().getUsername(),
                Instant.now()
        ));

        return saved;
    }
}
