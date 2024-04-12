package pl.sda.carrental.configuration.security.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.carrental.configuration.security.dto.UserDto;
import pl.sda.carrental.configuration.security.entity.Role;
import pl.sda.carrental.configuration.security.entity.User;
import pl.sda.carrental.configuration.security.repository.RoleRepository;
import pl.sda.carrental.configuration.security.repository.UserRepository;
import pl.sda.carrental.configuration.security.service.UserService;
import pl.sda.carrental.exceptionHandling.IllegalArgumentForEnumException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Client;
import pl.sda.carrental.model.Employee;
import pl.sda.carrental.model.enums.Position;
import pl.sda.carrental.repository.ClientRepository;
import pl.sda.carrental.repository.EmployeeRepository;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;

    @Override
    public UserDto saveClient(UserDto clientDTO) {
        Client client = new Client();
        client.setLogin(clientDTO.getLogin());
        client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));

        assignRoleToUser(client, "CLIENT");

        client.setEmail(clientDTO.getEmail());
        return mapToUserDto(clientRepository.save(client));
    }

    @Override
    public UserDto saveEmployee(UserDto employeeDTO) {
        Employee employee = new Employee();
        employee.setLogin(employeeDTO.getLogin());
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));

        if(!Arrays.stream(Position.values()).map(Enum::toString).toList().contains(employeeDTO.getPosition())) {
            throw new IllegalArgumentForEnumException("Invalid position");
        }

        String position = (userRepository.findAll().isEmpty()) ? "ADMIN" : employeeDTO.getPosition();
        assignRoleToUser(employee, position);

        employee.setPosition(Position.valueOf(position));
        return mapToUserDto(employeeRepository.save(employee));
    }

    private void assignRoleToUser(User user, String roleName) {
        Role role = roleRepository.findByName("ROLE_" + roleName);
        if (role == null) {
            role = createRoleIfNotExists(roleName);
        }
        user.setRoles(List.of(role));
    }

    @Override
    public UserDto findUserByLogin(String login) {
        return mapToUserDto(userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new ObjectNotFoundInRepositoryException("No user found with login: " + login)));
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }

    private Role createRoleIfNotExists(final String name) {
        Role role = new Role();
        role.setName("ROLE_" + name);
        return roleRepository.save(role);
    }

    private UserDto mapToUserDto(User user) {
        return new UserDto()
                .withId(user.getId())
                .withLogin(user.getLogin())
                .withPassword(user.getPassword());
    }
}
