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
import pl.sda.carrental.model.DTO.ReturnDTO;
import pl.sda.carrental.model.Returnal;
import pl.sda.carrental.service.ReturnService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Returnals")
public class ReturnController {
    private final ReturnService returnService;

    @Operation(summary = "Gets all returnals")
    @GetMapping("/manageL2/returnals")
    public List<Returnal> getReturnals() {
        return returnService.getAllReturnals();
    }

    @Operation(
            summary = "Adds new returnal",
            description = "Set of available employee IDs to chose from: {1, 2, 3, ..., 15" +
                    "<br>IDs of available reservations: {9, 10}"
    )
    @PostMapping("/manageL2/returnals")
    public Returnal saveReturnal(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReturnDTO.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleReturnalDTO",
                            value = "{\"employee\": 1," +
                                    " \"comments\": \"string\"," +
                                    " \"returnDate\": \"2024-12-12\"," +
                                    " \"upcharge\": 120," +
                                    " \"reservationId\": 9}"
                    )
            )
    )
                                 @RequestBody @Valid ReturnDTO returnDTO) {
        return returnService.saveReturn(returnDTO);
    }

    @Operation(
            summary = "Edits selected returnal",
            description = "Set of available employee IDs to chose from: {1, 2, 3, ..., 15" +
                    "<br>IDs of available reservations: {9, 10}" +
                    "<br>IDs of available returnals: {1, 2, 3, ..., 8}"
    )
    @PutMapping("/manageL2/returnals/{id}")
    public Returnal editReturnal(@Parameter(name = "id", example = "1", description = "returnal ID")
                                 @PathVariable Long id,
                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                         content = @Content(
                                                 mediaType = "application/json",
                                                 schema = @Schema(implementation = ReturnDTO.class),
                                                 examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                         name = "exampleReturnalDTO",
                                                         value = "{\"employee\": 1," +
                                                                 " \"comments\": \"string\"," +
                                                                 " \"returnDate\": \"2024-12-12\"," +
                                                                 " \"upcharge\": 120," +
                                                                 " \"reservationId\": 9}"
                                                 )
                                         )
                                 )
                                 @RequestBody ReturnDTO returnDTO) {
        return returnService.editReturnal(id, returnDTO);
    }

    @Operation(summary = "Deletes selected returnal")
    @DeleteMapping("/manageL2/returnals/{id}")
    public void deleteReturnal(@Parameter(name = "id", example = "1", description = "returnal ID")
                                   @PathVariable Long id) {
        returnService.deleteReturnalById(id);
    }
}
