package pl.sda.carrental.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.model.EmployeeModel;
import pl.sda.carrental.service.EmployeeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public List<EmployeeModel> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public EmployeeModel addEmployee(@RequestBody EmployeeModel employee) {
        return employeeService.addEmployee(employee);
    }

    @PutMapping("/{id}")
    public EmployeeModel editEmployee(@PathVariable Long id, @RequestBody EmployeeModel employee) {
        return employeeService.editEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
            employeeService.deleteEmployee(id);
    }

}
