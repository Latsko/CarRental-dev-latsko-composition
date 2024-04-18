package pl.sda.carrental.configuration.auth.service;

import pl.sda.carrental.configuration.auth.dto.ClientDto;
import pl.sda.carrental.configuration.auth.dto.EmployeeDto;
import pl.sda.carrental.configuration.auth.dto.UserDto;

import java.util.List;

public interface UserService {
    ClientDto saveClient(ClientDto client);
    EmployeeDto saveEmployee(EmployeeDto employee);
    UserDto findUserByLogin(String login);
    List<UserDto> findAllUsers();
}
