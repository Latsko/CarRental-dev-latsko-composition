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

    /**
     * Retrieves a list of all revenues from the database.
     *
     * @return List of Revenue objects representing all revenues in the database.
     */
    public List<Revenue> getRevenue() {
        return revenueRepository.findAll();
    }

    /**
     * Retrieves a specific revenue by its ID from the database.
     *
     * @param id The unique identifier of the revenue to retrieve.
     * @return The Revenue object associated with the specified ID.
     * @throws ObjectNotFoundInRepositoryException If no revenue is found with the given ID.
     */
    public Revenue getRevenueById(Long id) {
        return revenueRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No revenue under ID #" + id + "!"));
    }

    /**
     * Adds a new revenue entry to the database.
     *
     * @param revenue The Revenue object to be added.
     * @return The added Revenue object with its unique identifier.
     */
    public Revenue addRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }


    /**
     * Edits an existing revenue entry in the database.
     *
     * @param id      The unique identifier of the revenue to be edited.
     * @param revenue The Revenue object containing updated information.
     * @return The edited Revenue object with its updated details.
     * @throws ObjectNotFoundInRepositoryException If no revenue is found with the given ID.
     */
    public Revenue editRevenue(Long id, Revenue revenue) {
        Revenue foundRevenue = getRevenueById(id);
        foundRevenue.setTotalAmount(revenue.getTotalAmount());

        return revenueRepository.save(foundRevenue);
    }

    /**
     * Updates the total revenue amount for a specific revenue entry in the database.
     *
     * @param id           The unique identifier of the revenue to be updated.
     * @param rentalAmount The additional rental amount to be added to the existing total amount.
     * @throws ObjectNotFoundInRepositoryException If no revenue is found with the given ID.
     */
    public void updateRevenue(Long id, BigDecimal rentalAmount) {
        Revenue revenue = getRevenueById(id);
        BigDecimal updatedRevenue = revenue.getTotalAmount().add(rentalAmount);
        revenue.setTotalAmount(updatedRevenue);

        revenueRepository.save(revenue);
    }

    /**
     * Deletes a revenue entry from the database by its unique identifier.
     * Finds any branches associated with the revenue and if a branch is found, disassociate it from the revenue.
     *
     * @param id The unique identifier of the revenue to be deleted.
     * @throws ObjectNotFoundInRepositoryException If no revenue is found with the given ID.
     */
    public void deleteRevenue(Long id) {
        getRevenueById(id);
        Branch branch = branchRepository.findAll().stream()
                .filter(filteredBranch -> filteredBranch.getRevenue().getRevenueId().equals(id))
                .findFirst()
                .orElse(null);

        if (branch != null) {
            branch.setRevenue(null);
            branchRepository.save(branch);
        }
        revenueRepository.deleteById(id);
    }

    /**
     * Assigns a revenue entry to a branch based on their respective unique identifiers.
     *
     * @param revenueId The unique identifier of the revenue to be assigned.
     * @param branchId  The unique identifier of the branch to which the revenue will be assigned.
     * @throws ObjectNotFoundInRepositoryException    If no revenue or branch is found with the given IDs.
     * @throws ObjectAlreadyAssignedToBranchException If the branch already has an associated revenue.
     */
    public void assignRevenueToBranchByAccordingIds(Long revenueId, Long branchId) {
        Revenue foundRevenue = getRevenueById(revenueId);
        Branch foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId + "!"));

        if (foundBranch.getRevenue() != null) {
            throw new ObjectAlreadyAssignedToBranchException("Revenue already exists for branch under ID #" + branchId + "!");
        } else {
            foundBranch.setRevenue(foundRevenue);
            branchRepository.save(foundBranch);
        }
    }
}
