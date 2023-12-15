package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.exceptionHandling.ObjectAlreadyAssignedToBranchException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.Employee;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.repository.EmployeeRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final CarRepository carRepository;
    private final EmployeeRepository employeeRepository;

    public void addBranch(Branch branch) {
        branchRepository.save(branch);
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public void removeBranch(Long id) {
        branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under  ID #" + id));
        branchRepository.deleteById(id);
    }

    public Branch editBranch(Long id, Branch branch) {
        Branch found = branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under  ID #" + id));

        found.setAddress(branch.getAddress());
        found.setName(branch.getName());

        branchRepository.deleteById(id);

        return branchRepository.save(found);
    }

    /**
     *
     * @param id
     * @return
     */
    public Branch getById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + id));
    }

    public void addCarToBranchByAccordingId(Long id, Car car) {
        if (branchRepository.findAll().isEmpty()) {
            throw new ObjectNotFoundInRepositoryException("There are no created branches currently");
        }

        Branch foundBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + id));

        car.setBranch(foundBranch);
        carRepository.save(car);
        foundBranch.getCars().add(car);
    }

    public void removeCarFromBranch(Long carId, Long branchId) {
        Branch foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        Car foundCar = foundBranch.getCars().stream()
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
        Car foundCar = carRepository.findById(carId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No car under ID #" + carId));
        if(foundCar.getBranch() != null) {
            throw new ObjectAlreadyAssignedToBranchException("Car already assigned to existing branch!");
        }
        Branch foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        foundBranch.getCars().add(foundCar);
        foundCar.setBranch(foundBranch);

        branchRepository.save(foundBranch);
        carRepository.save(foundCar);
    }

    public void assignEmployeeToBranch(Long employeeId, Long branchId) {
        Employee foundEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No employee under ID #" + employeeId));
        if(foundEmployee.getBranch() != null) {
            throw new ObjectAlreadyAssignedToBranchException("Employee already assigned to existing branch!");
        }
        Branch foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        foundBranch.getEmployees().add(foundEmployee);
        foundEmployee.setBranch(foundBranch);

        branchRepository.save(foundBranch);
        employeeRepository.save(foundEmployee);
    }

    public void removeEmployeeFromBranch(Long employeeId, Long branchId) {
        Branch foundBranch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + branchId));

        Employee foundEmployee = foundBranch.getEmployees().stream()
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
