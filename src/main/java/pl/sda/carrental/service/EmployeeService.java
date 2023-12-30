package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Employee;
import pl.sda.carrental.repository.EmployeeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    /**
     * Gets all Employees objects
     *
     * @return List of all Employees
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


    /**
     * The addEmployee method is responsible for adding a new employee by saving the provided Employee object to the repository
     *
     * @param employee Object to be added to the repository
     * @return The newly created and saved employee object
     */
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * The editEmployee method is a transactional operation that allows for the modification of an existing employee based on the provided
     * employee ID and updated employee details. It retrieves the employee by ID from the repository, updates its details, deletes the
     * existing employee, and then saves the modified employee back to the repository
     *
     * @param id The identifier of the employee to be edited
     * @param employee  An object containing updated employee data
     * @return The modified employee object
     * @throws ObjectNotFoundInRepositoryException if no employee is found with the provided ID
     */
    public Employee editEmployee(Long id, Employee employee) {
        Employee edit = employeeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No employee under ID #" + id));

        edit.setName(employee.getName());
        edit.setSurname(employee.getSurname());
        edit.setPosition(employee.getPosition());
        edit.setBranch(employee.getBranch());
        employeeRepository.deleteById(id);

        return employeeRepository.save(edit);
    }

    /**
     * Deletes employee object using ID
     *
     * @param id The identifier of the Employee to be deleted
     * @throws ObjectNotFoundInRepositoryException if no employee is found with the provided ID
     */
    public void deleteEmployee(Long id) {
        employeeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No employee under that ID!"));
        employeeRepository.deleteById(id);
    }
}
