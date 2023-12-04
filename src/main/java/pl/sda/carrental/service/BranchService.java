package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.BranchModel;
import pl.sda.carrental.model.CarModel;
import pl.sda.carrental.model.CarRentalModel;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRentalRepository;
import pl.sda.carrental.repository.CarRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final CarRentalRepository carRentalRepository;

    public void addBranch(BranchModel branch) {
        branchRepository.save(branch);
    }

    public List<BranchModel> getAllBranches() {
        return branchRepository.findAll();
    }

    /*  WARNING     WARNING     WARNING     WARNING     WARNING     WARNING     */
    /*  Cannot use findById() for branches that are not assigned to CarRental   */
    /*  =====================================================================   */
    /*  Instead i create and delete it from CarRental perspective, where i am
     *   able to assign it to CarRental  */
    public void removeBranch(Long id) {
        branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under  ID #" + id));
        branchRepository.deleteById(id);
    }

    public BranchModel editBranch(Long id, BranchModel branchModel) {
        BranchModel found = branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under  ID #" + id));

        found.setAddress(branchModel.getAddress());
        found.setName(branchModel.getName());

        branchRepository.deleteById(id);

        return branchRepository.save(found);

    }

    // how to handle exceptions better
    public BranchModel getById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + id));
    }

    public void addCarToBranchByAccordingId(Long id, CarModel car) {
        if (branchRepository.findAll().isEmpty()) {
            throw new ObjectNotFoundInRepositoryException("There are no created branches currently");
        }

        BranchModel foundBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + id));

        car.setBranch(foundBranch);
        carRepository.save(car);
        foundBranch.getCars().add(car);
    }

    public void removeCarFromBranch(Long carId, Long branchId) {
        CarRentalModel carRentalModel = carRentalRepository.findAll().stream().findAny()
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("Car rental does not exist!"));

        BranchModel foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        CarModel foundCar = foundBranch.getCars().stream()
                .filter(car -> car.getCar_id() == carId)
                .findFirst()
                .orElseThrow(() ->
                        new ObjectNotFoundInRepositoryException("No car under ID #"
                                + carId + " is assigned to branch under ID #" + branchId));

        //neither removal from repository nor actualizing fields manually does work, don't know why
        // will try to modify it up to the root parent

        //copying set of cars and modifying it with removal of the car
        Set<CarModel> modifiedCarSet = new HashSet<>(foundBranch.getCars());
        modifiedCarSet.remove(foundCar);

        // copying branch and modifying that copy, giving new set of cars without that one car
        BranchModel modifiedBranch = new BranchModel(foundBranch.getBranch_id(),
                                                     foundBranch.getName(),
                                                     foundBranch.getAddress(),
                                                     foundBranch.getEmployees(),
                                                     modifiedCarSet,
                                                     foundBranch.getClients(),
                                                     foundBranch.getCarRental());

        // copying rental and modifying it branches field with new set of branches
        Set<BranchModel> modifiedBranches = new HashSet<>(carRentalModel.getBranches());
        modifiedBranches.remove(foundBranch);
        modifiedBranches.add(modifiedBranch);

        CarRentalModel modifiedRental = new CarRentalModel(carRentalModel.getCar_rental_id(),
                                                           carRentalModel.getName(),
                                                           carRentalModel.getDomain(),
                                                           carRentalModel.getAddress(),
                                                           carRentalModel.getOwner(),
                                                           carRentalModel.getLogo(),
                                                           modifiedBranches);

        // setting car branch to null
        foundCar.setBranch(null);

        carRentalRepository.deleteAll();
        carRentalRepository.save(modifiedRental);
    }
}
