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
    public UserDto findUserByName(@RequestParam String name) {
        return userService.findUserByName(name);
    }

    @GetMapping("/users")
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping
    public UserDto register(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }
}
