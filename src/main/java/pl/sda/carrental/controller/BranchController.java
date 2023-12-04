package pl.sda.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.BranchModel;
import pl.sda.carrental.model.CarModel;
import pl.sda.carrental.model.CarRentalModel;
import pl.sda.carrental.service.BranchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/branches")
public class BranchController {
    private final BranchService branchService;

    @GetMapping
    public List<BranchDTO> getBranches() {

        return branchService.getAllBranches().stream()
                .map(this::mapToBranchDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public BranchDTO getById(@PathVariable Long id) {
        BranchModel branch = branchService.getById(id);
        return mapToBranchDTO(branch);
    }

    private BranchDTO mapToBranchDTO(BranchModel branch) {
        CarRentalModel carRental = branch.getCarRental();

        if(branch.getCarRental() == null) {
            return new BranchDTO(
                    branch.getBranch_id(),
                    branch.getName(),
                    null);
        }

        return new BranchDTO(
                branch.getBranch_id(),
                branch.getName(),
                new HQDetails(
                        carRental.getName(),
                        carRental.getOwner(),
                        carRental.getDomain(),
                        carRental.getAddress()
                )
        );
    }

    @PostMapping
    public void addBranch(@RequestBody BranchModel branch) {
        branchService.addBranch(branch);
    }

    @PutMapping("/addCar/toBranchUnderId/{id}")
    public void addCarToBranch(@PathVariable Long id, @RequestBody CarModel car) {
        branchService.addCarToBranchByAccordingId(id, car);
    }

    @PutMapping("/{id}")
    public BranchModel modifyBranch(@PathVariable Long id, @RequestBody BranchModel branch) {
        return branchService.editBranch(id, branch);
    }

    @DeleteMapping("/{id}")
    public void removeBranch(@PathVariable Long id) {
        branchService.removeBranch(id);
    }

    @PatchMapping("deleteCar/{car_id}/fromBranch/{branch_id}")
    public void removeCarFromBranch(@PathVariable Long car_id, @PathVariable Long branch_id) {
        branchService.removeCarFromBranch(car_id, branch_id);
    }
}

record BranchDTO(Long branchId, String branchName, HQDetails mainBranchDetails) {
}

record HQDetails(String CarRentalName, String owner, String internetDomain, String address) {
}