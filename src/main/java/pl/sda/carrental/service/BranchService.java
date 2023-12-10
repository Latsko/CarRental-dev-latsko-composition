package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.BranchModel;
import pl.sda.carrental.model.CarModel;
import pl.sda.carrental.model.EmployeeModel;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.repository.ClientRepository;
import pl.sda.carrental.repository.EmployeeRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;

    public void addBranch(BranchModel branch) {
        branchRepository.save(branch);
    }

    public List<BranchModel> getAllBranches() {
        return branchRepository.findAll();
    }

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
        BranchModel foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        CarModel foundCar = foundBranch.getCars().stream()
                .filter(car -> Objects.equals(car.getCar_id(), carId))
                .findFirst()
                .orElseThrow(() ->
                        new ObjectNotFoundInRepositoryException("No car under ID #"
                                + carId + " is assigned to branch under ID #" + branchId));

        foundBranch.getCars().remove(foundCar);
        foundCar.setBranch(null);

        branchRepository.save(foundBranch);
        carRepository.save(foundCar);
    }

    public void assignCarToBranch(Long carId, Long branchId) {
        CarModel foundCar = carRepository.findById(carId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No car under ID #" + carId));
        if(foundCar.getBranch() != null) {
            throw new RuntimeException("Car already assigned to existing branch!");
        }
        BranchModel foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        foundBranch.getCars().add(foundCar);
        foundCar.setBranch(foundBranch);

        branchRepository.save(foundBranch);
        carRepository.save(foundCar);
    }

    public void assignEmployeeToBranch(Long employeeId, Long branchId) {
        EmployeeModel foundEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No employee under ID #" + employeeId));
        if(foundEmployee.getBranch() != null) {
            throw new RuntimeException("Car already assigned to existing branch!");
        }
        BranchModel foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        foundBranch.getEmployees().add(foundEmployee);
        foundEmployee.setBranch(foundBranch);

        branchRepository.save(foundBranch);
        employeeRepository.save(foundEmployee);
    }

    public void removeEmployeeFromBranch(Long employeeId, Long branchId) {
        BranchModel foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        EmployeeModel foundEmployee = foundBranch.getEmployees().stream()
                .filter(employee -> Objects.equals(employee.getEmployee_id(), employeeId))
                .findFirst()
                .orElseThrow(() ->
                        new ObjectNotFoundInRepositoryException("No employee under ID #"
                                + employeeId + " is assigned to branch under ID #" + branchId));

        foundBranch.getEmployees().remove(foundEmployee);
        foundEmployee.setBranch(null);

        branchRepository.save(foundBranch);
        employeeRepository.save(foundEmployee);
    }


}
