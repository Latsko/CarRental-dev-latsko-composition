package pl.sda.carrental.configuration.auth.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sda.carrental.configuration.auth.dto.ClientDto;
import pl.sda.carrental.configuration.auth.dto.EmployeeDto;
import pl.sda.carrental.configuration.auth.dto.UserDto;
import pl.sda.carrental.configuration.auth.model.Client;
import pl.sda.carrental.configuration.auth.model.Employee;
import pl.sda.carrental.configuration.auth.model.Role;
import pl.sda.carrental.configuration.auth.model.User;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.configuration.auth.repository.RoleRepository;
import pl.sda.carrental.configuration.auth.repository.UserRepository;
import pl.sda.carrental.configuration.auth.service.UserService;
import pl.sda.carrental.exceptionHandling.IllegalArgumentForEnumException;
import pl.sda.carrental.exceptionHandling.IllegalArgumentFullNameException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.enums.Position;
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

    /**
     * Finds a user by their login.
     *
     * @param login The login of the user to find.
     * @return The UserDto object representing the user with the specified login.
     * @throws ObjectNotFoundInRepositoryException if no user is found with the specified login.
     */
    @Override
    public UserDto findUserByLogin(String login) {
        return mapToUserDto(userRepository.findByLogin(login)
                .orElseThrow(() ->
                        new ObjectNotFoundInRepositoryException("No user found with login: " + login)));
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of UserDto objects representing all users in the repository.
     */
    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }

    /**
     * Saves a new client based on the provided ClientDto.
     *
     * @param clientDTO The ClientDto object representing the client to be saved.
     * @return A ClientDto object representing the saved client.
     */
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

    /**
     * Saves a new employee based on the provided EmployeeDto.
     *
     * @param employeeDTO The EmployeeDto object representing the employee to be saved.
     * @return An EmployeeDto object representing the saved employee.
     * @throws IllegalArgumentForEnumException if the position provided in the EmployeeDto is invalid.
     */
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

    /**
     * Sets the fields of a User object based on the provided UserDto.
     *
     * @param user    The User object whose fields are to be set.
     * @param userDto The UserDto object containing the data to set the User fields.
     * @throws ObjectNotFoundInRepositoryException if no branch is found under the provided branchId.
     */
    private void setUserFields(User user, UserDto userDto) {
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(!userDto.getFullName().contains(" ")) {
            throw new IllegalArgumentFullNameException("Full name must contain spaces!");
        }
        user.setName(userDto.getFullName().split(" ")[0]);
        user.setSurname(userDto.getFullName().split(" ")[1]);
        user.setBranch(branchRepository.findById(userDto.getBranchId())
                .orElseThrow(() -> new ObjectNotFoundInRepositoryException("No branch under ID #" + userDto.getBranchId())));

    }

    /**
     * Assigns a role to the specified user based on the provided role name.
     * If the role does not exist in the database, it creates a new role with the specified name.
     *
     * @param user     The User to whom the role is to be assigned.
     * @param roleName The name of the role to assign to the user.
     */
    private void assignRoleToUser(User user, String roleName) {
        Role role = roleRepository.findByName("ROLE_" + roleName);
        if (role == null) {
            role = createRoleIfNotExists(roleName);
        }
        user.setRoles(List.of(role));
    }

    /**
     * Creates a new role with the specified name if it doesn't already exist in the database.
     *
     * @param name The name of the role to create.
     * @return The created Role object.
     */
    private Role createRoleIfNotExists(final String name) {
        Role role = new Role();
        role.setName("ROLE_" + name);
        return roleRepository.save(role);
    }

    /**
     * Maps a User entity to a UserDto object.
     *
     * @param user The User entity to map.
     * @return The mapped UserDto object.
     */
    private UserDto mapToUserDto(User user) {
        return new UserDto()
                .withId(user.getId())
                .withLogin(user.getLogin())
                .withPassword(user.getPassword())
                .withFullName(user.getName() + " " + user.getSurname())
                .withBranchId(user.getBranch().getBranchId());
    }

    /**
     * Maps a Client entity to a ClientDto object.
     *
     * @param client The Client entity to map.
     * @return The mapped ClientDto object.
     */
    private ClientDto mapToClientDto(Client client) {
        ClientDto clientDto = new ClientDto();

        clientDto.setEmail(client.getEmail());
        clientDto.setAddress(client.getAddress());

        setUserDtoFields(client, clientDto);

        return clientDto;
    }

    /**
     * Maps an Employee entity to an EmployeeDto object.
     *
     * @param employee The Employee entity to map.
     * @return The mapped EmployeeDto object.
     */
    private EmployeeDto mapToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setPosition(String.valueOf(employee.getPosition()));
        setUserDtoFields(employee, employeeDto);

        return employeeDto;
    }

    /**
     * Sets the fields of a UserDto object based on the fields of a User entity.
     *
     * @param user    The User entity containing the data.
     * @param userDto The UserDto object to set the fields for.
     */
    private void setUserDtoFields(User user, UserDto userDto) {
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setFullName(user.getName() + " " + user.getSurname());
        userDto.setBranchId(user.getBranch().getBranchId());
    }
}
