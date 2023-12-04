package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.ReservationModel;

public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {
}
