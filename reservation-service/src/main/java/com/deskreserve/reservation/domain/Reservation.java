package com.deskreserve.reservation.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reservations",
        indexes = {
                @Index(name = "idx_reservations_employee", columnList = "employee_id"),
                @Index(name = "idx_reservations_desk", columnList = "desk_id")
        })
public class Reservation {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "desk_id", nullable = false)
    private Desk desk;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.ACTIVE;

    protected Reservation() {}

    public Reservation(UUID id, Desk desk, Employee employee, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.desk = desk;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = ReservationStatus.ACTIVE;
    }

    public UUID getId() { return id; }
    public Desk getDesk() { return desk; }
    public Employee getEmployee() { return employee; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public ReservationStatus getStatus() { return status; }

    public void cancel() { this.status = ReservationStatus.CANCELLED; }
}
