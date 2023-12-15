package pl.sda.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.carrental.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
}
