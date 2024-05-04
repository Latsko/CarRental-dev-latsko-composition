package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.DTO.ReturnDTO;
import pl.sda.carrental.model.Returnal;
import pl.sda.carrental.service.ReturnService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReturnController {
    private final ReturnService returnService;

    @GetMapping("/manageL2/returnals")
    public List<Returnal> getReturnals() {
        return returnService.getAllReturnals();
    }

    @PostMapping("/manageL2/returnals")
    public Returnal saveReturnal(@RequestBody @Valid ReturnDTO returnDTO) {
        return returnService.saveReturn(returnDTO);
    }

    @PutMapping("/manageL2/returnals/{id}")
    public Returnal editReturnal(@PathVariable Long id, @RequestBody ReturnDTO returnDTO) {
        return returnService.editReturnal(id, returnDTO);
    }

    @DeleteMapping("/manageL2/returnals/{id}")
    public void deleteReturnal(@PathVariable Long id) {
        returnService.deleteReturnalById(id);
    }
}
