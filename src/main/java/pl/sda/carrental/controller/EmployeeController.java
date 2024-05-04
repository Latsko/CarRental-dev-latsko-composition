package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.auth.model.Employee;
import pl.sda.carrental.service.EmployeeService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/manageL1/employees")
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @PutMapping("/manageL1/employees/{id}")
    public Employee editEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.editEmployee(id, employee);
    }

    @DeleteMapping("/manageL1/employees/{id}")
    public void deleteEmployee(@PathVariable Long id) {
            employeeService.deleteEmployee(id);
    }

}
