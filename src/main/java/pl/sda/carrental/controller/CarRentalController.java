package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.CarRental;
import pl.sda.carrental.service.CarRentalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CarRentalController {
    private final CarRentalService carRentalService;

    @GetMapping("/public/carRental")
    public CarRental getCarRental(){
        return carRentalService.getCarRental();
    }

    @PostMapping("/admin/carRental")
    public CarRental addCarRental(@RequestBody @Valid CarRental carRental){
        return carRentalService.saveCarRental(carRental);
    }

    @PostMapping("/admin/carRental/addBranch")
    public Branch openBranch(@RequestBody @Valid Branch branch) {
        return carRentalService.openNewBranch(branch);
    }

    @PutMapping("/admin/carRental")
    public CarRental editCarRental(@RequestBody CarRental carRental){
        return carRentalService.editCarRental(carRental);
    }

    @DeleteMapping("/admin/carRental")
    public void deleteCarRental(){
        carRentalService.deleteCarRental();
    }

    @DeleteMapping("/admin/carRental/deleteBranch/{id}")
    public void closeBranch(@PathVariable Long id) {
        carRentalService.closeBranchUnderId(id);
    }
}
