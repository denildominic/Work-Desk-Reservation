\
package com.deskreserve.reservation.repo;

import com.deskreserve.reservation.domain.Employee;
import com.deskreserve.reservation.domain.Reservation;
import com.deskreserve.reservation.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByEmployeeOrderByStartDateDesc(Employee employee);

    @Query("select (count(r) > 0) " +
           "from Reservation r " +
           "where r.desk.id = :deskId " +
           "  and r.status = :status " +
           "  and not (r.endDate < :startDate or r.startDate > :endDate)")
    boolean existsOverlapping(Long deskId, ReservationStatus status, LocalDate startDate, LocalDate endDate);
}
