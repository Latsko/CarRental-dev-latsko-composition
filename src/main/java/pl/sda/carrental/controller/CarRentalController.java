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
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.CarRental;
import pl.sda.carrental.service.CarRentalService;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Car Rental")
public class CarRentalController {
    private final CarRentalService carRentalService;

    @Operation(summary = "Gets Car Rental")
    @GetMapping("/public/carRental")
    public CarRental getCarRental() {
        return carRentalService.getCarRental();
    }

    @Operation(summary = "Creates the Car Rental")
    @PostMapping("/admin/carRental")
    public CarRental addCarRental(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CarRental.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleCarRental",
                            value = "{\"name\": \"string\", \"domain\": \"string\", \"address\": \"string\"," +
                                    " \"owner\": \"string\", \"logo\": \"string\"}"

                    )
            )
    )
                                  @RequestBody @Valid CarRental carRental) {
        return carRentalService.saveCarRental(carRental);
    }

    @Operation(summary = "Opens new branch in rental")
    @PostMapping("/admin/carRental/addBranch")
    public Branch openBranch(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Branch.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleBranchDTO",
                            value = "{\"name\": \"branchName\", \"address\": \"branchAddress\"}"
                    )
            )
    )
            @RequestBody @Valid Branch branch) {
        return carRentalService.openNewBranch(branch);
    }

    @Operation(summary = "Edits Car Rental")
    @PutMapping("/admin/carRental")
    public CarRental editCarRental(@RequestBody @Valid CarRental carRental) {
        return carRentalService.editCarRental(carRental);
    }

    @Operation(
            summary = "Removes Car Rental",
            description = "When Car Rental is deleted, all data is removed"
    )
    @DeleteMapping("/admin/carRental")
    public void deleteCarRental() {
        carRentalService.deleteCarRental();
    }

    @Operation(summary = "Closes selected branch",
            description = "Set of available branch IDs to chose from: {1, 2, 3}" +
            "<br>When a branch is deleted, all assigned cars, employees and clients are deleted as well"
    )
    @DeleteMapping("/admin/carRental/deleteBranch/{id}")
    public void closeBranch(@Parameter(name = "id", example = "1", description = "branch ID")
            @PathVariable Long id) {
        carRentalService.closeBranchUnderId(id);
    }
}
