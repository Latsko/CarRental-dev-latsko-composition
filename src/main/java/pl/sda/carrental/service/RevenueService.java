package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.exceptionHandling.ObjectAlreadyAssignedToBranchException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Revenue;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.RevenueRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final RevenueRepository revenueRepository;
    private final BranchRepository branchRepository;

    public List<Revenue> getRevenue() {
        return revenueRepository.findAll();
    }

    public Revenue getById(Long id) {
        return revenueRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No revenue under ID #" + id + "!"));
    }

    public Revenue addRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }


    public Revenue editRevenue(Long id, Revenue revenue) {
        Revenue foundRevenue = revenueRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No revenue under ID #" + id + "!"));

        foundRevenue.setTotalAmount(revenue.getTotalAmount());

        return revenueRepository.save(foundRevenue);
    }

    public void updateRevenue(Long id, BigDecimal rentalAmount) {
        Revenue revenue = revenueRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundInRepositoryException("No revenue under ID #" + id + "!"));
        BigDecimal updatedRevenue = revenue.getTotalAmount().add(rentalAmount);
        revenue.setTotalAmount(updatedRevenue);

        revenueRepository.save(revenue);
    }

    public void deleteRevenue(Long id) {
        revenueRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No revenue under ID #" + id + "!"));
//        Branch branch = branchRepository.findAll().stream()
//                .filter(filteredBranch -> filteredBranch.getRevenue().getRevenue_id().equals(id))
//                .findFirst()
//                .orElse(null);
//
//        if(branch != null) {
//            branch.setRevenue(null);
//            branchRepository.save(branch);
//        }
        revenueRepository.deleteById(id);
    }

//    public void assignRevenueToBranchByAccordingIds(Long revenueId, Long branchId) {
//        Revenue foundRevenue = revenueRepository.findById(revenueId)
//                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No revenue under ID #" + revenueId + "!"));
//        Branch foundBranch = branchRepository.findById(branchId)
//                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId + "!"));
//
//        if(foundBranch.getRevenue() != null) {
//            throw new ObjectAlreadyAssignedToBranchException("Revenue already exists for branch under ID #" + branchId + "!");
//        } else {
//            foundBranch.setRevenue(foundRevenue);
//            branchRepository.save(foundBranch);
//        }
//    }
}
