package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.ReservationDTO;
import pl.sda.carrental.model.ReservationModel;
import pl.sda.carrental.service.ReservationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationModel> getReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping
    public ReservationModel saveReservation(@RequestBody @Valid ReservationDTO reservation) {
        return reservationService.saveReservation(reservation);
    }

    @PutMapping("/{id}")
    public ReservationModel editReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        return reservationService.editReservation(id, reservationDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
    }
}
