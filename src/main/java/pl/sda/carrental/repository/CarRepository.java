package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.CarModel;

public interface CarRepository extends JpaRepository<CarModel, Long> {
}
