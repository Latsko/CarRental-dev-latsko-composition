package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.Revenue;
import pl.sda.carrental.service.RevenueService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping("/api")
public class RevenueController {
    private final RevenueService revenueService;

    @Operation(summary = "Gets all revenues")
    @GetMapping("/admin/revenues")
    public List<Revenue> getRevenue() {
        return revenueService.getAllRevenues();
    }

    @Operation(summary = "Adds new revenue")
    @PostMapping("/admin/revenues")
    public Revenue addRevenue(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Revenue.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleRevenue",
                            value = "{\"totalAmount\": 0}"
                    )
            )
    )
                              @RequestBody Revenue revenue) {
        return revenueService.addRevenue(revenue);
    }

    @Operation(
            summary = "Edits selected revenue",
            description = "List of available revenue IDs: {1, 2, 3}"
    )
    @PutMapping("/admin/revenues/{id}")
    public Revenue editRevenue(@Parameter(name = "id", example = "1", description = "revenue ID")
                               @PathVariable Long id,
                               @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                       content = @Content(
                                               mediaType = "application/json",
                                               schema = @Schema(implementation = Revenue.class),
                                               examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                       name = "exampleRevenue",
                                                       value = "{\"totalAmount\": 0}"
                                               )
                                       )
                               ) @RequestBody Revenue revenue) {
        return revenueService.editRevenue(id, revenue);
    }

    @Operation(
            summary = "Assigns existing revenue to a selected branch",
            description = "List of available branch IDs: {1, 2, 3}" +
                    "<br>Please delete revenue from branch under ID #1 to assign new revenue"
    )
    @PatchMapping("/admin/revenues/assignRevenue/{revenue_id}/toBranch/{branch_id}")
    public void assignRevenueToBranch(@Parameter(name = "revenue_id", example = "4", description = "available Revenue ID")
            @PathVariable Long revenue_id,
            @Parameter(name = "branch_id", example = "1", description = "branch ID")
            @PathVariable Long branch_id) {
        revenueService.assignRevenueToBranchByAccordingIds(revenue_id, branch_id);
    }

    @Operation(
            summary = "Deletes selected revenue",
            description = "List of available revenue IDs: {1, 2, 3}"
    )
    @DeleteMapping("/admin/revenues/{id}")
    public void deleteRevenue(@Parameter(name = "id", example = "1", description = "revenue ID")
                              @PathVariable Long id) {
        revenueService.deleteRevenue(id);
    }
}
