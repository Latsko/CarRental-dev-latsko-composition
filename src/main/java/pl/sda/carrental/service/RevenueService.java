package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Revenue;
import pl.sda.carrental.repository.RevenueRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final RevenueRepository revenueRepository;

    public List<Revenue> getRevenue() {
        return revenueRepository.findAll();
    }

    public Revenue addRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }


    public Revenue editRevenue(Long id, Revenue revenue) {
        Revenue edit = revenueRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No revenue under ID" + id + "!"));

        edit.setAmount(revenue.getAmount());
        revenueRepository.deleteById(id);

        return revenueRepository.save(edit);
    }

    public void deleteRevenue(Long id) {
        revenueRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No revenue under ID" + id + "!"));

        revenueRepository.deleteById(id);
    }
}
