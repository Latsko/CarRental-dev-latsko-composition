package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.CarRental;

public interface CarRentalRepository extends JpaRepository<CarRental, Long> {

}
