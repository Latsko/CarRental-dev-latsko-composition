package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.BranchModel;
import pl.sda.carrental.model.CarRentalModel;
import pl.sda.carrental.service.CarRentalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carRental")
public class CarRentalController {
    private final CarRentalService carRentalService;

    @GetMapping
    public CarRentalModel getCarRental(){
        return carRentalService.getCarRental();
    }
    @PostMapping
    public void addCarRental(@RequestBody @Valid CarRentalModel carRentalModel){
        carRentalService.saveCarRental(carRentalModel);
    }

    @PostMapping("/addBranch")
    public void openBranch(@RequestBody @Valid BranchModel branch) {
        carRentalService.openNewBranch(branch);
    }

    @PutMapping
    public void editCarRental(@RequestBody CarRentalModel carRentalModel){
        carRentalService.editCarRental(carRentalModel);
    }

    @DeleteMapping
    public void deleteCarRental(){
        carRentalService.deleteCarRental();
    }

    @DeleteMapping("/deleteBranch/{id}")
    public void closeBranch(@PathVariable Long id) {
        carRentalService.deleteBranchUnderId(id);
    }
}
