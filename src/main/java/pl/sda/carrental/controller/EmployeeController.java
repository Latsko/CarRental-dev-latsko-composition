package pl.sda.carrental.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.auth.model.Employee;
import pl.sda.carrental.service.EmployeeService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@Tag(name = "Employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Operation(summary = "Gets all employees")
    @GetMapping("/manageL1/employees")
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @Operation(summary = "Edits selected employee",
            description = "Available employee IDs to choose from: {1, 2, 3, ..., 15}")
    @PutMapping("/manageL1/employees/{id}")
    public Employee editEmployee(@Parameter(name = "id", example = "1", description = "employee ID")
                                 @PathVariable Long id,
                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                         content = @Content(
                                                 mediaType = "application/json",
                                                 schema = @Schema(implementation = Employee.class),
                                                 examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                                         name = "exampleEmployee",
                                                         value = "{\"login\": \"string\"," +
                                                                 " \"password\": \"string\"," +
                                                                 " \"name\": \"string\"," +
                                                                 " \"surname\": \"string\"," +
                                                                 " \"position\": \"EMPLOYEE\"}"

                                                 )
                                         )
                                 )
                                 @RequestBody Employee employee) {
        return employeeService.editEmployee(id, employee);
    }

    @Operation(summary = "Deletes selected employee",
            description = "Available employee IDs to choose from: {1, 2, 3, ..., 15}")
    @DeleteMapping("/manageL1/employees/{id}")
    public void deleteEmployee(@Parameter(name = "id", example = "1", description = "employee ID")
                               @PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

}
