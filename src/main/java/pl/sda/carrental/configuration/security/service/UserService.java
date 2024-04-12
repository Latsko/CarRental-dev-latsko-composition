package pl.sda.carrental.configuration.security.service;

import pl.sda.carrental.configuration.security.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveClient(UserDto client);
    UserDto saveEmployee(UserDto employee);
    UserDto findUserByLogin(String login);
    List<UserDto> findAllUsers();
}
