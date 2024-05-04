package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/authenticated/reservations")
    public List<ReservationDTO> getReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping("/authenticated/reservations")
    public Reservation saveReservation(@RequestBody @Valid ReservationDTO reservation) {
        return reservationService.saveReservation(reservation);
    }

    @PutMapping("/authenticated/reservations/{id}")
    public Reservation editReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        return reservationService.editReservation(id, reservationDTO);
    }

    @DeleteMapping("/authenticated/reservations/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
    }

    @PatchMapping("/authenticated/reservations/{id}")
    public void cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservationById(id);
    }
}
