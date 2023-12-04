package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.BranchModel;
import pl.sda.carrental.model.CarRentalModel;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRentalRepository;

@Service
@RequiredArgsConstructor
public class CarRentalService {
    private final CarRentalRepository carRentalRepository;
    private final BranchRepository branchRepository;

    public CarRentalModel getCarRental() {
        CarRentalModel carRentalModel = carRentalRepository.findAll().stream().findFirst().orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no car rental company"));
        return carRentalModel;
    }

    public void saveCarRental(CarRentalModel carRental) {
        carRentalRepository.save(carRental);
    }


    public void editCarRental(CarRentalModel carRentalModel) {
        CarRentalModel edited = carRentalRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no car rental company to edit"));

        edited.setName(carRentalModel.getName());
        edited.setAddress(carRentalModel.getAddress());
        edited.setOwner(carRentalModel.getOwner());
        edited.setLogo(carRentalModel.getLogo());

        carRentalRepository.deleteAll();

        carRentalRepository.save(edited);
    }

    public void deleteCarRental() {
        carRentalRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no car rental company"));

        carRentalRepository.deleteAll();
    }

    public void openNewBranch(BranchModel branch) {
        // check branch already exists in given city

        CarRentalModel carRentalModel = carRentalRepository.findById(1L)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no rental company"));
        branch.setCarRental(carRentalModel);

        if(!carRentalRepository.findAll().isEmpty()) {
            branchRepository.save(branch);
            carRentalRepository.findAll().get(0).getBranches().add(branch);
        } else {
            throw new ObjectNotFoundInRepositoryException("There are no Car Rentals for branch to be assigned to!");
        }
    }

    public void deleteBranchUnderId(Long id) {
        branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("There is no branch under ID #" + id));

        branchRepository.deleteById(id);
    }
}
