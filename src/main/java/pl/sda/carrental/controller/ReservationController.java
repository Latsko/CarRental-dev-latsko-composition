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
import pl.sda.carrental.model.DTO.ReservationDTO;
import pl.sda.carrental.model.Reservation;
import pl.sda.carrental.service.ReservationService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "Gets all reservations")
    @GetMapping("/authenticated/reservations")
    public List<ReservationDTO> getReservations() {
        return reservationService.getAllReservations();
    }

    @Operation(
            summary = "Creates new reservation",
            description = "Set of customers IDs: {16, 17, 18, ..., 30}" +
                    "<br>Set of car IDs: {1, 2, 3, ..., 20}" +
                    "<br>Set of branches IDs: {1, 2, 3}"
    )
    @PostMapping("/authenticated/reservations")
    public Reservation saveReservation(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReservationDTO.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleReservationDTO",
                            value = "{\"customer_id\": 20," +
                                    " \"car_id\": 20," +
                                    " \"startDate\": \"2024-12-12\"," +
                                    " \"endDate\": \"2024-12-17\"," +
                                    " \"startBranchId\": 1," +
                                    " \"endBranchId\": 3}"
                    )
            )
    )
                                       @RequestBody @Valid ReservationDTO reservation) {
        return reservationService.saveReservation(reservation);
    }

    @Operation(
            summary = "Edits selected reservation",
            description = "Set of customers IDs: {16, 17, 18, ..., 30}" +
                    "<br>Set of car IDs: {1, 2, 3, ..., 20}" +
                    "<br>Set of branches IDs: {1, 2, 3}" +
                    "<br>Set of reservation IDs: {1, 2, 3, ..., 10}"
    )
    @PutMapping("/authenticated/reservations/{id}")
    public Reservation editReservation(@Parameter(name = "id", example = "10", description = "reservation ID")
                                       @PathVariable Long id,
                                       @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                               content = @Content(
                                                       mediaType = "application/json",
                                                       schema = @Schema(implementation = ReservationDTO.class),
                                                       examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                               name = "exampleReservationDTO",
                                                               value = "{\"customer_id\": 20," +
                                                                       " \"car_id\": 20," +
                                                                       " \"startDate\": \"2024-12-12\"," +
                                                                       " \"endDate\": \"2024-12-17\"," +
                                                                       " \"startBranchId\": 1," +
                                                                       " \"endBranchId\": 3}"
                                                       )
                                               )
                                       )
                                       @RequestBody ReservationDTO reservationDTO) {
        return reservationService.editReservation(id, reservationDTO);
    }

    @Operation(
            summary = "Deletes selected reservation",
            description = "Set of reservation IDs: {1, 2, 3, ..., 10}"
    )
    @DeleteMapping("/authenticated/reservations/{id}")
    public void deleteReservation(@Parameter(name = "id", example = "10", description = "reservation ID")
                                  @PathVariable Long id) {
        reservationService.deleteReservationById(id);
    }

    @Operation(
            summary = "Cancels selected reservation",
            description = "Set of reservation IDs: {1, 2, 3, ..., 10}"
    )
    @PatchMapping("/authenticated/reservations/cancel/{id}")
    public void cancelReservation(@Parameter(name = "id", example = "10", description = "reservation ID")
                                  @PathVariable Long id) {
        reservationService.cancelReservationById(id);
    }
}
