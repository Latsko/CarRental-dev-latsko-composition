package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.service.CarService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping("/authenticated/cars/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @GetMapping("/public/cars")
    public List<Car> getCars(){
        return carService.getCars();
    }

    @GetMapping("/authenticated/cars/statusOnDate/{id}") Status getCarStatusOnDate(@PathVariable Long id, LocalDate date) {
        return carService.getStatusOnDateForCarUnderId(id, date);
    }

    @PostMapping("/manageL1/cars")
    public Car addCar(@RequestBody @Valid Car car) {
        return carService.addCar(car);
    }

    @PutMapping("/manageL1/cars/{id}")
    public Car editCar(@PathVariable Long id, @RequestBody @Valid Car car) {
        return carService.editCar(id, car);
    }

    @PatchMapping("/manageL2/cars/setMileageAndPrice/{id}")
    public Car updateMileageAndPrice(@RequestParam double mileage, @RequestParam BigDecimal price, @PathVariable Long id) {
        return carService.updateMileageAndPrice(mileage, price, id);
    }

    @PatchMapping("/manageL2/cars/setStatus/{id}")
    public Car updateStatus(@RequestParam String status, @PathVariable Long id) {
        return carService.updateStatus(status, id);
    }

    @DeleteMapping("/manageL1/cars/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCarById(id);
    }
}
