package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.CarModel;
import pl.sda.carrental.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping("/{id}")
    public CarModel getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @GetMapping
    public List<CarModel> getCars(){
        return carService.getCars();
    }

    @PostMapping
    public void addCar(@RequestBody @Valid CarModel carModel) {
        carService.addCar(carModel);
    }

    @PutMapping("/{id}")
    public void editCar(@PathVariable Long id, @RequestBody @Valid CarModel carModel) {
        carService.editCar(id, carModel);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCarById(id);
    }
}
