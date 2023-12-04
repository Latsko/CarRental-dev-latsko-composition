package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.RevenueModel;
import pl.sda.carrental.repository.RevenueRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final RevenueRepository revenueRepository;

    public List<RevenueModel> getRevenue() {
        return revenueRepository.findAll();
    }

    public RevenueModel addRevenue(RevenueModel revenue) {
        return revenueRepository.save(revenue);
    }


    public RevenueModel editRevenue(Long id, RevenueModel revenue) {
        RevenueModel edit = revenueRepository.findById(id)
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
