package pl.sda.carrental.configuration.security.service;

import pl.sda.carrental.configuration.security.dto.UserDto;
import pl.sda.carrental.configuration.security.entity.User;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto user);
    UserDto findUserByName(String name);
    List<UserDto> findAllUsers();
}
