package pl.sda.carrental.configuration.security.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.sda.carrental.configuration.security.dto.UserDto;
import pl.sda.carrental.configuration.security.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
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
    public UserDto registerClient(@RequestBody UserDto userDto) {
        return userService.saveClient(userDto);
    }

    @PostMapping("/newEmployee")
    public UserDto registerEmployee(@RequestBody UserDto userDto) {
        return userService.saveEmployee(userDto);
    }
}
