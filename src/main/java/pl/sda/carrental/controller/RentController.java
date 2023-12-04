package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.RentDTO;
import pl.sda.carrental.model.RentModel;
import pl.sda.carrental.service.RentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rents")
public class RentController {
    private final RentService rentService;

    @PostMapping
    public RentModel save(@RequestBody @Valid RentDTO rent) {
        return rentService.save(rent);
    }
}
