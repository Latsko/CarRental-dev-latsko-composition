package pl.sda.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sda.carrental.model.ReturnDTO;
import pl.sda.carrental.model.ReturnModel;
import pl.sda.carrental.service.ReturnService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/return")
public class ReturnController {
    private final ReturnService returnService;

    @PostMapping
    public ReturnModel save(@RequestBody @Valid ReturnDTO returnDTO) {
        return returnService.saveReturn(returnDTO);
    }
}
