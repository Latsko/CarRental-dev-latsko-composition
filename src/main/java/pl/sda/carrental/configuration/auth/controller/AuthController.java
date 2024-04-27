package pl.sda.carrental.configuration.auth.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.auth.dto.ClientDto;
import pl.sda.carrental.configuration.auth.dto.EmployeeDto;
import pl.sda.carrental.configuration.auth.dto.UserDto;
import pl.sda.carrental.configuration.auth.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @GetMapping
    public UserDto findUserByLogin(@RequestParam String login) {
        return userService.findUserByLogin(login);
    }

    @GetMapping("/users")
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/newClient")
    public ClientDto registerClient(@RequestBody ClientDto clientDto) {
        return userService.saveClient(clientDto);
    }

    @PostMapping("/newEmployee")
    public EmployeeDto registerEmployee(@RequestBody EmployeeDto employeeDto) {
        return userService.saveEmployee(employeeDto);
    }
}
