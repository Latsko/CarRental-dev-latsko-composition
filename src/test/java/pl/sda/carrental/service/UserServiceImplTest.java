package pl.sda.carrental.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sda.carrental.configuration.auth.dto.ClientDto;
import pl.sda.carrental.configuration.auth.dto.EmployeeDto;
import pl.sda.carrental.configuration.auth.dto.UserDto;
import pl.sda.carrental.configuration.auth.entity.Client;
import pl.sda.carrental.configuration.auth.entity.Employee;
import pl.sda.carrental.configuration.auth.entity.Role;
import pl.sda.carrental.configuration.auth.entity.User;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.configuration.auth.repository.RoleRepository;
import pl.sda.carrental.configuration.auth.repository.UserRepository;
import pl.sda.carrental.configuration.auth.service.impl.UserServiceImpl;
import pl.sda.carrental.exceptionHandling.IllegalArgumentForEnumException;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.enums.Position;
import pl.sda.carrental.repository.BranchRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class UserServiceImplTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
    private final ClientRepository clientRepository = mock(ClientRepository.class);
    private final BranchRepository branchRepository = mock(BranchRepository.class);

    private final UserServiceImpl userService = new UserServiceImpl(
            userRepository,
            roleRepository,
            passwordEncoder,
            employeeRepository,
            clientRepository,
            branchRepository);

    private Branch branch;
    private User employee;
    private User client;
    private ClientDto clientDto;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        branch = new Branch().withBranchId(1L);
        employee = new Employee(1L, "eLogin", "ePassword",
                null, null, branch, new ArrayList<>(), Position.EMPLOYEE);
        client = new Client(2L, "cLogin", "cPassword",
                null, null, branch, new ArrayList<>(),
                "cEmial@gmail.com", null);
        clientDto = new ClientDto(
                client.getId(),
                client.getLogin(),
                client.getPassword(),
                client.getName() + " " + employee.getSurname(),
                client.getBranch().getBranchId(),
                ((Client) client).getEmail(),
                ((Client) client).getAddress());
        employeeDto = new EmployeeDto(
                employee.getId(),
                employee.getLogin(),
                employee.getPassword(),
                employee.getName() + " " + employee.getSurname(),
                employee.getBranch().getBranchId(),
                ((Employee) employee).getPosition().toString());
    }

    @Test
    public void shouldFindUserByLogin() {
        //given
        when(userRepository.findByLogin("eLogin")).thenReturn(Optional.of(employee));

        //when
        UserDto foundByLogin = userService.findUserByLogin("eLogin");

        //then
        assertThat(foundByLogin)
                .isNotNull()
                .isInstanceOf(UserDto.class);
        assertThat(foundByLogin.getLogin()).isEqualTo("eLogin");
    }

    @Test
    public void shouldNotFindUserByLogin() {
        //given
        when(userRepository.findByLogin("eLogin")).thenReturn(Optional.empty());

        //when
       ThrowableAssert.ThrowingCallable callable = () -> userService.findUserByLogin("eLogin");

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No user found with login: eLogin");
    }

    @Test
    public void shouldFindAllUsers() {
        //given
        when(userRepository.findAll()).thenReturn(List.of(employee, client));

        //when
        List<UserDto> allUsers = userService.findAllUsers();

        //then
        assertThat(allUsers)
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void shouldSaveClient() {
        //given
        when(passwordEncoder.encode(anyString())).thenReturn(client.getPassword());
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(roleRepository.findByName(anyString())).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(new Role(1L , "ROLE_CLIENT", null));
        when(clientRepository.save(any(Client.class))).thenReturn((Client) client);

        //when
        ClientDto saved = userService.saveClient(clientDto);

        //then
        assertThat(saved)
                .isNotNull()
                .isInstanceOf(ClientDto.class);
        assertThat(saved.getLogin()).isEqualTo("cLogin");
        assertThat(saved.getPassword()).isEqualTo("cPassword");
    }

    @Test
    public void shouldNotSaveClientWithIncorrectBranchId() {
        //given
        when(passwordEncoder.encode(anyString())).thenReturn(client.getPassword());
        when(branchRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> userService.saveClient(clientDto);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No branch under ID #1");
    }

    @Test
    public void shouldSaveEmployee() {
        //given
        when(passwordEncoder.encode(anyString())).thenReturn(employee.getPassword());
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(roleRepository.findByName(anyString())).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(new Role(1L , "ROLE_CLIENT", null));
        when(employeeRepository.save(any(Employee.class))).thenReturn((Employee) employee);

        //when
        EmployeeDto saved = userService.saveEmployee(employeeDto);

        //then
        assertThat(saved)
                .isNotNull()
                .isInstanceOf(EmployeeDto.class);
        assertThat(saved.getPosition()).isEqualTo(Position.EMPLOYEE.toString());
        assertThat(saved.getLogin()).isEqualTo("eLogin");
        assertThat(saved.getPassword()).isEqualTo("ePassword");
    }

    @Test
    public void shouldNotSaveEmployeeWithInvalidPosition() {
        //given
        employeeDto.setPosition("INVALID");
        when(passwordEncoder.encode(anyString())).thenReturn(employee.getPassword());
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> userService.saveEmployee(employeeDto);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentForEnumException.class)
                .hasMessage("Invalid position");
    }
}