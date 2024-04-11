package pl.sda.carrental.configuration.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.security.dto.UserDto;
import pl.sda.carrental.configuration.security.service.UserService;
import pl.sda.carrental.service.ClientService;
import pl.sda.carrental.service.EmployeeService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final ClientService clientService;
    private final EmployeeService employeeService;

    @GetMapping
    public UserDto findUserByName(@RequestParam String name) {
        return userService.findUserByName(name);
    }

    @GetMapping("/users")
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/newClient")
    public UserDto registerClient(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @PostMapping("/newEmployee")
    public UserDto registerEmployee(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }
}
