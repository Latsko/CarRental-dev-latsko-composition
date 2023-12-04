package pl.sda.carrental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sda.carrental.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.EmployeeModel;
import pl.sda.carrental.repository.EmployeeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;


    public List<EmployeeModel> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeModel addEmployee(EmployeeModel employee) {
        return employeeRepository.save(employee);
    }

    public EmployeeModel editEmployee(Long id, EmployeeModel employee) {
        EmployeeModel edit = employeeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No employee under that id!"));

        edit.setName(employee.getName());
        edit.setSurname(employee.getSurname());
        edit.setPosition(employee.getPosition());
        //edit.setBranch(employee.getBranch());
        employeeRepository.deleteById(id);

        return employeeRepository.save(edit);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No employee under that ID!"));
        employeeRepository.deleteById(id);
    }
}
