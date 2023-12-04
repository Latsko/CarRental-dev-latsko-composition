package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sda.carrental.model.ReservationDTO;
import pl.sda.carrental.model.ReservationModel;
import pl.sda.carrental.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ReservationModel saveReservation(@RequestBody @Valid ReservationDTO reservation) {
        return reservationService.saveReservation(reservation);
    }
}
