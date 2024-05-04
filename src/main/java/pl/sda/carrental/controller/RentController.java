package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
public class RentController {
    private final RentService rentService;

    @GetMapping("/manageL2/rents")
    public List<Rent> getAllRents() {
        return rentService.getAllRents();
    }

    @PostMapping("/authenticated/rents")
    public Rent save(@RequestBody @Valid RentDTO rent) {
        return rentService.saveRent(rent);
    }

    @PutMapping("/authenticated/rents/{id}")
    public Rent editRent(@PathVariable Long id, @RequestBody RentDTO rent) {
        return rentService.editRent(id, rent);
    }

    @DeleteMapping("/authenticated/rents/{id}")
    public void deleteRent(@PathVariable Long id) {
        rentService.deleteRentById(id);
    }
}
