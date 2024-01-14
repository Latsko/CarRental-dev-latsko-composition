package pl.sda.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.Revenue;
import pl.sda.carrental.service.RevenueService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/revenues")
public class RevenueController {
    private final RevenueService revenueService;

    @GetMapping
    public List<Revenue> getRevenue() {
        return revenueService.getRevenue();
    }

    @PostMapping
    public Revenue addRevenue(@RequestBody Revenue revenue) {
        return revenueService.addRevenue(revenue);
    }

    @PutMapping("/{id}")
    public Revenue editRevenue(@PathVariable Long id ,@RequestBody Revenue revenue) {
        return revenueService.editRevenue(id, revenue);
    }

    @PatchMapping("/assignRevenue/{revenue_id}/toBranch/{branch_id}")
    public void assignRevenueToBranch(@PathVariable Long revenue_id, @PathVariable Long branch_id) {
        revenueService.assignRevenueToBranchByAccordingIds(revenue_id, branch_id);
    }

    @DeleteMapping("/{id}")
    public void deleteRevenue(@PathVariable Long id) {
        revenueService.deleteRevenue(id);
    }
}
