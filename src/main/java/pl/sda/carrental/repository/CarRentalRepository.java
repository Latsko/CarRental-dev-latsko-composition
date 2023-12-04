package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.CarRentalModel;

public interface CarRentalRepository extends JpaRepository<CarRentalModel, Long> {

}
