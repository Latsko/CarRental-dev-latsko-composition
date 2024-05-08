package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.auth.model.Employee;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.CarRental;
import pl.sda.carrental.model.DTO.CarDTO;
import pl.sda.carrental.service.BranchService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping(value = "api/")
@Tag(name = "Branches")
public class BranchController {
    private final BranchService branchService;

    @Operation(
            summary = "Gets all branches"
    )
    @GetMapping("/public/branches")
    public List<BranchDTO> getBranches() {
        return branchService.getAllBranches().stream()
                .map(this::mapToBranchDTO)
                .toList();
    }

    @Operation(
            summary = "Gets branch by ID",
            description = "Gets branch by ID. Enter an ID from set: {1, 2, 3}"
    )
    @GetMapping("/authenticated/branches/{id}")
    public BranchDTO getById(@Parameter(name = "id", example = "1", description = "branch ID")
                             @PathVariable Long id) {
        Branch branch = branchService.getById(id);
        return mapToBranchDTO(branch);
    }

    @Operation(
            summary = "Gets available cars on a certain date at branch under given ID",
            description = "Set of available branches IDs to chose from: {1, 2, 3}" +
                    "\nProvided date may be chosen arbitrary."
    )
    @GetMapping("/authenticated/branches/{id}/availableCarsOnDate/{date}")
    public List<CarDTO> getCarsAvailableOnDate(@Parameter(name = "id", example = "1", description = "branch ID")
                                               @PathVariable Long id,
                                               @Parameter(name = "date", example = "2024-01-10")
                                               @PathVariable String date) {
        return branchService.getCarsAvailableAtBranchOnDate(id, date);
    }

    private BranchDTO mapToBranchDTO(Branch branch) {
        CarRental carRental = branch.getCarRental();

        if (branch.getCarRental() == null) {
            return new BranchDTO(
                    branch.getBranchId(),
                    branch.getName(),
                    null);
        }

        return new BranchDTO(
                branch.getBranchId(),
                branch.getName(),
                new HQDetails(
                        carRental.getName(),
                        carRental.getOwner(),
                        carRental.getDomain(),
                        carRental.getAddress()
                )
        );
    }

    @Operation(
            summary = "Adds a new branch"
    )
    @PostMapping("/admin/branches")
    public Branch addBranch(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BranchDTO.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleBranchDTO",
                            value = "{\"name\": \"branchName\", \"address\": \"branchAddress\"}"
                    )
            )
    )
                            @RequestBody Branch branch) {
        return branchService.addBranch(branch);
    }

    @Operation(
            summary = "Adds a new car to an existing branch",
            description = "Set of available branches IDs to chose from: {1, 2, 3}"
    )
    @PutMapping("/admin/branches/addCar/toBranchUnderId/{id}")
    public Car addCarToBranch(@Parameter(name = "id", example = "1", description = "branch ID")
                              @PathVariable Long id,
                              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                      content = @Content(
                                              mediaType = "application/json",
                                              schema = @Schema(implementation = Car.class),
                                              examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                      name = "exampleCar",
                                                      value = "{\"make\": \"string\"," +
                                                              " \"model\": \"string\", \"bodyStyle\":" +
                                                              " \"string\", \"yearOfManufacture\": 1990," +
                                                              " \"colour\": \"string\", \"mileage\": 20000," +
                                                              " \"status\": \"AVAILABLE\", \"price\": 100}"
                                              )
                                      )
                              )
                              @RequestBody Car car) {
        return branchService.addCarToBranchByAccordingId(id, car);
    }

    @Operation(
            summary = "Edits selected branch",
            description = "Set of available branch IDs to chose from: {1, 2, 3}" +
                    "<br>Set of IDs for manager field: {1, 2, 3, ... ,15}"
    )
    @PutMapping("/admin/branches/{id}")
    public Branch editBranch(@Parameter(name = "id", example = "1", description = "branch ID")
                             @PathVariable Long id,
                             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                     content = @Content(
                                             mediaType = "application/json",
                                             schema = @Schema(implementation = Branch.class),
                                             examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                     name = "exampleBranch",
                                                     value = "{\"name\": \"string\"," +
                                                             " \"address\": \"string\"," +
                                                             " \"managerId\": 15}"
                                             )
                                     )
                             )
                             @RequestBody Branch branch) {
        return branchService.editBranch(id, branch);
    }

    @Operation(
            summary = "Deletes selected branch",
            description = "Set of available branch IDs to chose from: {1, 2, 3}" +
                    "<br>When a branch is deleted, all assigned cars, employees and clients are deleted as well"
    )
    @DeleteMapping("/admin/branches/{id}")
    public void removeBranch(@Parameter(name = "id", example = "1", description = "branch ID")
                             @PathVariable Long id) {
        branchService.removeBranch(id);
    }

    @Operation(
            summary = "Removes a car from a branch by according IDs",
            description = "Car IDs assigned to a branch with ID #1: {1, 4, 7, 10, 13, 16, 19}" +
                    "<br>Car IDs assigned to a branch with ID #2: {2, 5, 8, 11, 14, 17, 20}" +
                    "<br>Car IDs assigned to a branch with ID #3: {3, 6, 9, 12, 15, 18}"
    )
    @PatchMapping("/manageL1/branches/removeCar/{car_id}/fromBranch/{branch_id}")
    public void removeCarFromBranch(@Parameter(name = "car_id", example = "1", description = "car ID")
                                    @PathVariable Long car_id,
                                    @Parameter(name = "branch_id", example = "1", description = "branch ID")
                                    @PathVariable Long branch_id) {
        branchService.removeCarFromBranch(car_id, branch_id);
    }

    @Operation(
            summary = "Assigns a car to a branch by according IDs",
            description = "In order to successfully assign a car to a branch, please make sure that the car " +
                    "under selected ID is not assigned to another branch"
    )
    @PatchMapping("/manageL1/branches/assignCar/{car_id}/toBranch/{branch_id}")
    public Car assignCarToBranch(@Parameter(name = "car_id", example = "1", description = "car ID")
                                 @PathVariable Long car_id,
                                 @Parameter(name = "branch_id", example = "1", description = "branch ID")
                                 @PathVariable Long branch_id) {
        return branchService.assignCarToBranch(car_id, branch_id);
    }

    @Operation(
            summary = "Removes an employee from a branch by according IDs",
            description = "Employee IDs assigned to a branch with ID #1: {3, 6, 9, 12, 15}" +
                    "<br>Employee IDs assigned to a branch with ID #2: {1, 4, 7, 10, 13}" +
                    "<br>Employee IDs assigned to a branch with ID #3: {2, 5, 8, 11, 14}"
    )
    @PatchMapping("/manageL1/branches/removeEmployee/{employee_id}/fromBranch/{branch_id}")
    public void removeEmployeeFromBranch(@Parameter(name = "employee_id", example = "3", description = "employee ID")
                                         @PathVariable Long employee_id,
                                         @Parameter(name = "branch_id", example = "1", description = "branch ID")
                                         @PathVariable Long branch_id) {
        branchService.removeEmployeeFromBranch(employee_id, branch_id);
    }

    @Operation(
            summary = "Assigns an employee to a branch by according IDs",
            description = "In order to successfully assign an employee to a branch, please make sure that the employee " +
                    "under selected ID is not assigned to another branch"
    )
    @PatchMapping("/manageL1/branches/assignEmployee/{employee_id}/toBranch/{branch_id}")
    public Employee assignEmployeeToBranch(@Parameter(name = "employee_id", example = "3", description = "employee ID")
                                           @PathVariable Long employee_id,
                                           @Parameter(name = "branch_id", example = "1", description = "branch ID")
                                           @PathVariable Long branch_id) {
        return branchService.assignEmployeeToBranch(employee_id, branch_id);
    }

    @Operation(
            summary = "Assigns an employee as a manager to a branch by according IDs",
            description = "In order to successfully make an employee the manager of a branch, employee" +
                    "has to be assigned to that branch"
    )
    @PatchMapping("/admin/branches/assignManager/{manager_id}/forBranch/{branch_id}")
    public Branch assignManagerForBranch(@Parameter(name = "manager_id", example = "3", description = "manager ID")
                                         @PathVariable Long manager_id,
                                         @Parameter(name = "branch_id", example = "1", description = "branch ID")
                                         @PathVariable Long branch_id) {
        return branchService.addManagerForBranch(manager_id, branch_id);
    }

    @Operation(
            summary = "Fires a manager from a selected branch",
            description = "In order to remove a manager, branch has to have one"
    )
    @PatchMapping("/admin/branches/removeManagerFromBranch/{branch_id}")
    public void removeManagerFromBranch(@Parameter(name = "branch_id", example = "1", description = "branch ID")
                                        @PathVariable Long branch_id) {
        branchService.removeManagerFromBranch(branch_id);
    }
}

record BranchDTO(Long branchId, String branchName, HQDetails mainBranchDetails) {
}

record HQDetails(String CarRentalName, String owner, String internetDomain, String address) {
}