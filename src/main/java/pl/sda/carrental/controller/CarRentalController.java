package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.CarRental;
import pl.sda.carrental.service.CarRentalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carRental")
public class CarRentalController {
    private final CarRentalService carRentalService;

    @GetMapping
    public CarRental getCarRental(){
        return carRentalService.getCarRental();
    }
    @PostMapping
    public CarRental addCarRental(@RequestBody @Valid CarRental carRental){
        return carRentalService.saveCarRental(carRental);
    }

    @PostMapping("/addBranch")
    public Branch openBranch(@RequestBody @Valid Branch branch) {
        return carRentalService.openNewBranch(branch);
    }

    @PutMapping
    public CarRental editCarRental(@RequestBody CarRental carRental){
        return carRentalService.editCarRental(carRental);
    }

    @DeleteMapping
    public void deleteCarRental(){
        carRentalService.deleteCarRental();
    }

    @DeleteMapping("/deleteBranch/{id}")
    public void closeBranch(@PathVariable Long id) {
        carRentalService.closeBranchUnderId(id);
    }
}
