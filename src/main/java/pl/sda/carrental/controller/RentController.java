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
import pl.sda.carrental.model.DTO.RentDTO;
import pl.sda.carrental.model.Rent;
import pl.sda.carrental.service.RentService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Rentals")
public class RentController {
    private final RentService rentService;

    @Operation(summary = "Gets all rentals")
    @GetMapping("/manageL2/rents")
    public List<Rent> getAllRents() {
        return rentService.getAllRents();
    }

    @Operation(summary = "Creates rental",
            description = "Set of available employee IDs to chose from: {1, 2, 3, ..., 15}" +
                    "<br>IDs of available reservations: {9, 10}")
    @PostMapping("/authenticated/rents")
    public Rent save(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RentDTO.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleBranch",
                            value = "{\"employeeId\": 1," +
                                    " \"comments\": \"string\"," +
                                    " \"rentDate\": \"2024-12-12\"," +
                                    " \"reservationId\": 9}"
                    )
            )
    )
                     @RequestBody @Valid RentDTO rent) {
        return rentService.saveRent(rent);
    }

    @Operation(summary = "Edits selected rental",
            description = "Set of available employee IDs to chose from: {1, 2, 3, ..., 15}" +
                    "<br>IDs of available reservations: {9, 10}")
    @PutMapping("/authenticated/rents/{id}")
    public Rent editRent(@Parameter(name = "id", example = "8", description = "rental ID")
                         @PathVariable Long id,
                         @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                 content = @Content(
                                         mediaType = "application/json",
                                         schema = @Schema(implementation = RentDTO.class),
                                         examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                 name = "exampleBranch",
                                                 value = "{\"employeeId\": 2," +
                                                         " \"comments\": \"edited string\"," +
                                                         " \"rentDate\": \"2024-12-21\"," +
                                                         " \"reservationId\": 10}"
                                         )
                                 )
                         ) @RequestBody RentDTO rent) {
        return rentService.editRent(id, rent);
    }

    @Operation(summary = "Deletes selected rental")
    @DeleteMapping("/authenticated/rents/{id}")
    public void deleteRent(@Parameter(name = "id", example = "5", description = "rental ID")
                           @PathVariable Long id) {
        rentService.deleteRentById(id);
    }
}
