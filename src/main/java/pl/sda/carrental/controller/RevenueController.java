package pl.sda.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.RevenueModel;
import pl.sda.carrental.service.RevenueService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/revenue")
public class RevenueController {
    private final RevenueService revenueService;

    @GetMapping
    public List<RevenueModel> getRevenue() {
        return revenueService.getRevenue();
    }

    @PostMapping
    public RevenueModel addRevenue(@RequestBody RevenueModel revenue) {
        return revenueService.addRevenue(revenue);
    }

    @PutMapping("/{id}")
    public RevenueModel editRevenue(@PathVariable Long id, @RequestBody RevenueModel revenue) {
        return revenueService.editRevenue(id, revenue);
    }

    @DeleteMapping ("/{id}")
    void deleteRevenue(@PathVariable Long id) {
        revenueService.deleteRevenue(id);
    }
}
