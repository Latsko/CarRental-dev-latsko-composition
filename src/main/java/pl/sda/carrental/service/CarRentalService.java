package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.exceptionHandling.BranchAlreadyOpenInCityException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.CarRental;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRentalRepository;

@Service
@RequiredArgsConstructor
public class CarRentalService {
    private final CarRentalRepository carRentalRepository;
    private final BranchRepository branchRepository;

    public CarRental getCarRental() {
        return carRentalRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no car rental company"));
    }

    public void saveCarRental(CarRental carRental) {
        carRentalRepository.save(carRental);
    }


    public void editCarRental(CarRental carRental) {
        CarRental edited = carRentalRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no car rental company to edit"));

        edited.setName(carRental.getName());
        edited.setAddress(carRental.getAddress());
        edited.setOwner(carRental.getOwner());
        edited.setLogo(carRental.getLogo());

        carRentalRepository.deleteAll();

        carRentalRepository.save(edited);
    }

    public void deleteCarRental() {
        carRentalRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no car rental company"));

        carRentalRepository.deleteAll();
    }

    public void openNewBranch(Branch branch) {
        long branchAlreadyOpenInCity = carRentalRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Car Rental has not been created yet"))
                .getBranches()
                .stream()
                .map(Branch::getName)
                .filter(address -> address.equals(branch.getAddress()))
                .count();
        if(branchAlreadyOpenInCity > 0) {
            throw new BranchAlreadyOpenInCityException("Branch " + branch.getName() + " is already open in city"
            + branch.getAddress());
        }
            branchRepository.save(branch);
            carRentalRepository.findAll().get(0).getBranches().add(branch);
    }

    public void deleteBranchUnderId(Long id) {
        branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no branch under ID #" + id));

        branchRepository.deleteById(id);
    }
}
