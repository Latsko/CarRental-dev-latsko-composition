package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
