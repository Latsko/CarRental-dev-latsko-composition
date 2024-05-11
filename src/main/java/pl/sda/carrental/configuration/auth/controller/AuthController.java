package pl.sda.carrental.configuration.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.auth.dto.ClientDto;
import pl.sda.carrental.configuration.auth.dto.EmployeeDto;
import pl.sda.carrental.configuration.auth.dto.UserDto;
import pl.sda.carrental.configuration.auth.service.UserService;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authorization")
public class AuthController {
    private final UserService userService;

    @Operation(summary = "Gets user by its login")
    @GetMapping
    public UserDto findUserByLogin(@Parameter(name = "login", example = "admin", description = "user login")
                                   @RequestParam String login) {
        return userService.findUserByLogin(login);
    }

    @Operation(summary = "Gets all users")
    @GetMapping("/users")
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @Operation(summary = "Creates new client")
    @PostMapping("/newClient")
    public ClientDto registerClient(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ClientDto.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleClientDTO",
                            value = "{\"login\": \"string\"," +
                                    " \"password\": \"string\"," +
                                    " \"fullName\": \"string string\"," +
                                    " \"branchId\": 1," +
                                    " \"email\": \"string\"," +
                                    " \"address\": \"string\"}"
                    )
            )
    )
                                    @RequestBody ClientDto clientDto) {
        return userService.saveClient(clientDto);
    }

    @Operation(summary = "Creates new employee")
    @PostMapping("/newEmployee")
    public EmployeeDto registerEmployee(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EmployeeDto.class),
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            name = "exampleEmployeeDTO",
                            value = "{\"login\": \"string\"," +
                                    " \"password\": \"string\"," +
                                    " \"fullName\": \"string string\"," +
                                    " \"branchId\": 1," +
                                    " \"position\": \"EMPLOYEE\"}"
                    )
            )
    )
                                        @RequestBody EmployeeDto employeeDto) {
        return userService.saveEmployee(employeeDto);
    }
}
