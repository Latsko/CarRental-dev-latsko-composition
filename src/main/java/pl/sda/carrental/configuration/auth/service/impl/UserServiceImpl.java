package pl.sda.carrental.configuration.auth.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.carrental.configuration.auth.dto.ClientDto;
import pl.sda.carrental.configuration.auth.dto.EmployeeDto;
import pl.sda.carrental.configuration.auth.dto.UserDto;
import pl.sda.carrental.configuration.auth.entity.Role;
import pl.sda.carrental.configuration.auth.entity.User;
import pl.sda.carrental.configuration.auth.repository.RoleRepository;
import pl.sda.carrental.configuration.auth.repository.UserRepository;
import pl.sda.carrental.configuration.auth.service.UserService;
import pl.sda.carrental.exceptionHandling.IllegalArgumentForEnumException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.configuration.auth.entity.Client;
import pl.sda.carrental.configuration.auth.entity.Employee;
import pl.sda.carrental.model.enums.Position;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.repository.BranchRepository;

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
    private final BranchRepository branchRepository;

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

    @Override
    @Transactional
    public ClientDto saveClient(ClientDto clientDTO) {
        Client client = new Client();
        setUserFields(client, clientDTO);
        assignRoleToUser(client, "CLIENT");

        client.setAddress(clientDTO.getAddress());
        client.setEmail(clientDTO.getEmail());

        return mapToClientDto(clientRepository.save(client));
    }

    @Override
    @Transactional
    public EmployeeDto saveEmployee(EmployeeDto employeeDTO) {
        Employee employee = new Employee();

        setUserFields(employee, employeeDTO);

        if(!Arrays.stream(Position.values()).map(Enum::toString).toList().contains(employeeDTO.getPosition())) {
            throw new IllegalArgumentForEnumException("Invalid position");
        }

        String position = (userRepository.findAll().isEmpty()) ? "ADMIN" : employeeDTO.getPosition();
        assignRoleToUser(employee, position);

        employee.setPosition(Position.valueOf(position));
        return mapToEmployeeDto(employeeRepository.save(employee));
    }

    private void setUserFields(User user, UserDto userDto) {
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getFullName().split(" ")[0]);
        user.setSurname(userDto.getFullName().split(" ")[1]);
        user.setBranch(branchRepository.findById(userDto.getBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + userDto.getBranchId())));

    }

    private void assignRoleToUser(User user, String roleName) {
        Role role = roleRepository.findByName("ROLE_" + roleName);
        if (role == null) {
            role = createRoleIfNotExists(roleName);
        }
        user.setRoles(List.of(role));
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
                .withPassword(user.getPassword())
                .withFullName(user.getName() + " " + user.getSurname())
                .withBranchId(user.getBranch().getBranchId());
    }

    private ClientDto mapToClientDto(Client client) {
        ClientDto clientDto = new ClientDto();

        clientDto.setEmail(client.getEmail());
        clientDto.setAddress(client.getAddress());

        setUserDtoFields(client, clientDto);

        return clientDto;
    }

    private EmployeeDto mapToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setPosition(String.valueOf(employee.getPosition()));
        setUserDtoFields(employee, employeeDto);

        return employeeDto;
    }

    private void setUserDtoFields(User user, UserDto userDto) {
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setFullName(user.getName() + " " + user.getSurname());
        userDto.setBranchId(user.getBranch().getBranchId());
    }
}
