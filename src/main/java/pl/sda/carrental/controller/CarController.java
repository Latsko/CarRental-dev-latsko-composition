package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Cars")
public class CarController {
    private final CarService carService;

    @Operation(summary = "Gets a car by ID")
    @GetMapping("/authenticated/cars/{id}")
    public Car getCarById(@Parameter(name = "id", example = "1", description = "car ID")
                          @PathVariable Long id) {
        return carService.getCarById(id);
    }

    @Operation(summary = "Gets all cars")
    @GetMapping("/public/cars")
    public List<Car> getCars() {
        return carService.getCars();
    }

    @Operation(summary = "Gets car status on a given date")
    @GetMapping("/authenticated/cars/statusOnDate/{id}")
    Status getCarStatusOnDate(@Parameter(name = "id", example = "1", description = "car ID")
                              @PathVariable Long id,
                              @Parameter(example = "2024-01-10", description = "example date")
                              LocalDate date) {
        return carService.getStatusOnDateForCarUnderId(id, date);
    }

    @Operation(summary = "Adds new car")
    @PostMapping("/manageL1/cars")
    public Car addCar(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Car.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleCar",
                            value = """
                                    {
                                      "make": "string",
                                      "model": "string",
                                      "bodyStyle": "string",
                                      "yearOfManufacture": 2000,
                                      "colour": "string",
                                      "mileage": 10000,
                                      "status": "AVAILABLE",
                                      "price": 10000
                                    }"""
                    )
            )
    )
                      @RequestBody
                      @Valid Car car) {
        return carService.addCar(car);
    }

    @Operation(summary = "Edits an existing car")
    @PutMapping("/manageL1/cars/{id}")
    public Car editCar(@Parameter(name = "id", example = "1", description = "car ID")
                       @PathVariable Long id,
                       @io.swagger.v3.oas.annotations.parameters.RequestBody(
                               content = @Content(
                                       mediaType = "application/json",
                                       schema = @Schema(implementation = Car.class),
                                       examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                               name = "exampleCar",
                                               value = """
                                                       {
                                                         "make": "edited",
                                                         "model": "edited",
                                                         "bodyStyle": "edited",
                                                         "yearOfManufacture": 2000,
                                                         "colour": "edited",
                                                         "mileage": 10000,
                                                         "status": "AVAILABLE",
                                                         "price": 100
                                                       }"""
                                       )
                               )
                       )
                       @RequestBody @Valid Car car) {
        return carService.editCar(id, car);
    }

    @Operation(summary = "Sets mileage and price for a car")
    @PatchMapping("/manageL2/cars/setMileageAndPrice/{id}")
    public Car updateMileageAndPrice(@Parameter(example = "11111")
                                     @RequestParam double mileage,
                                     @Parameter(example = "111")
                                     @RequestParam BigDecimal price,
                                     @Parameter(name = "id", example = "1", description = "car ID")
                                     @PathVariable Long id) {
        return carService.updateMileageAndPrice(mileage, price, id);
    }

    @Operation(summary = "Sets Status for a car",
            description = "Supported statuses for a car: {RENTED, AVAILABLE, UNAVAILABLE, INSPECTION, MALFUNCTION}")
    @PatchMapping("/manageL2/cars/setStatus/{id}")
    public Car updateStatus(@Parameter(example = "INSPECTION")
                            @RequestParam String status,
                            @Parameter(name = "id", example = "1", description = "car ID")
                            @PathVariable Long id) {
        return carService.updateStatus(status, id);
    }

    @Operation(summary = "Deletes a car",
    description = "When a car is deleted all reservations, rents and returnals for that car are also removed")
    @DeleteMapping("/manageL1/cars/{id}")
    public void deleteCar(@Parameter(name = "id", example = "1", description = "car ID")
                          @PathVariable Long id) {
        carService.deleteCarById(id);
    }
}
