//package pl.sda.carrental.service;
//
//import org.assertj.core.api.ThrowableAssert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
//import pl.sda.carrental.model.Branch;
//import pl.sda.carrental.model.Employee;
//import pl.sda.carrental.model.Rent;
//import pl.sda.carrental.model.Returnal;
//import pl.sda.carrental.model.enums.Position;
//import pl.sda.carrental.repository.BranchRepository;
//import pl.sda.carrental.repository.EmployeeRepository;
//import pl.sda.carrental.repository.RentRepository;
//import pl.sda.carrental.repository.ReturnRepository;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//class EmployeeServiceTest {
//    private final EmployeeRepository employeeRepositoryMock = mock(EmployeeRepository.class);
//    private final RentRepository rentRepositoryMock = mock(RentRepository.class);
//    private final ReturnRepository returnRepositoryMock = mock(ReturnRepository.class);
//    private final BranchRepository branchRepositoryMock = mock(BranchRepository.class);
//
//    private final EmployeeService employeeService = new EmployeeService(employeeRepositoryMock, rentRepositoryMock,
//            returnRepositoryMock, branchRepositoryMock);
//
//    private Branch branch;
//    private Employee employee;
//
//    @BeforeEach
//    void setUp() {
//        branch = new Branch();
//        employee = new Employee(1L, "name", "surname", Position.ENTRY, null);
//    }
//
//    @Test
//    void shouldGetAllEmployeesWithNonEmptyList() {
//        //given
//        List<Employee> employees = Collections.singletonList(employee);
//        when(employeeRepositoryMock.findAll()).thenReturn(employees);
//
//        //when
//        List<Employee> allEmployees = employeeService.getAllEmployees();
//
//        //then
//        assertThat(allEmployees)
//                .isNotEmpty()
//                .containsExactly(employee)
//                .hasSize(1);
//    }
//
//    @Test
//    void shouldGetAllEmployeesWithEmptyList() {
//        //given
//        when(employeeRepositoryMock.findAll()).thenReturn(new ArrayList<>());
//
//        //when
//        List<Employee> allEmployees = employeeService.getAllEmployees();
//
//        //then
//        assertThat(allEmployees).isEmpty();
//    }
//
//    @Test
//    void shouldAddEmployee() {
//        //given
//        when(employeeRepositoryMock.save(any(Employee.class))).thenReturn(employee);
//
//        //when
//        Employee savedEmployee = employeeService.addEmployee(employee);
//
//        //then
//        assertThat(savedEmployee)
//                .isNotNull()
//                .isInstanceOf(Employee.class)
//                .isEqualTo(employee);
//    }
//
//    @Test
//    void shouldEditEmployee() {
//        //given
//        employee.setBranch(branch);
//        branch.getEmployees().add(employee);
//        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));
//        when(employeeRepositoryMock.save(any(Employee.class))).thenReturn(employee);
//        when(branchRepositoryMock.save(any(Branch.class))).thenReturn(branch);
//        when(employeeRepositoryMock.save(any(Employee.class))).thenReturn(employee);
//
//        //when
//        Employee editedEmployee = employeeService.editEmployee(1L, new Employee(1L,
//                "editedName",
//                "editedSurname",
//                Position.MANAGER,
//                null));
//
//        //then
//        assertThat(editedEmployee).isNotNull();
//        assertThat(editedEmployee.getEmployeeId()).isEqualTo(1L);
//        assertThat(editedEmployee.getName()).isEqualTo("editedName");
//        assertThat(editedEmployee.getSurname()).isEqualTo("editedSurname");
//        assertThat(editedEmployee.getPosition()).isEqualTo(Position.MANAGER);
//    }
//
//    @Test
//    void shouldNotEditEmployeeWhenEmployeeDoesNotExist() {
//        //given
//        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
//
//        //when
//        ThrowableAssert.ThrowingCallable callable = () -> employeeService.editEmployee(1L, new Employee());
//
//        //then
//        assertThatThrownBy(callable)
//                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
//                .hasMessage("No employee under ID #1");
//        verify(branchRepositoryMock, never()).save(any(Branch.class));
//        verify(employeeRepositoryMock, never()).save(any(Employee.class));
//    }
//
//    @Test
//    void shouldNotEditEmployeeWhenThereIsNoEmployeeInBranch() {
//        //given
//        employee.setBranch(branch);
//        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));
//
//        //when
//        ThrowableAssert.ThrowingCallable callable = () -> employeeService.editEmployee(1L, new Employee());
//
//        //then
//        assertThatThrownBy(callable)
//                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
//                .hasMessage("No employee under ID #1 in that branch");
//        verify(branchRepositoryMock, never()).save(any(Branch.class));
//        verify(employeeRepositoryMock, never()).save(any(Employee.class));
//    }
//
//    @Test
//    void shouldDeleteEmployeeWhenThereAreNoRentsAndReturnalsMadeByGivenEmployee() {
//        //given
//        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));
//        when(rentRepositoryMock.findRentsByEmployeeId(anyLong())).thenReturn(new ArrayList<>());
//        when(returnRepositoryMock.findReturnalsByEmployeeId(anyLong())).thenReturn(new ArrayList<>());
//        when(rentRepositoryMock.save(any(Rent.class))).thenReturn(null);
//        when(returnRepositoryMock.save(any(Returnal.class))).thenReturn(null);
//        doNothing().when(employeeRepositoryMock).deleteById(anyLong());
//
//        //when
//        employeeService.deleteEmployee(1L);
//
//        //then
//        verify(employeeRepositoryMock, times(1)).deleteById(1L);
//
//    }
//
//    @Test
//    void shouldDeleteEmployeeWhenThereAreRentsAndReturnalsMadeByGivenEmployee() {
//        //given
//        Rent rent = new Rent().withEmployee(employee);
//        Returnal returnal = new Returnal().withEmployee(employee);
//        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));
//        when(rentRepositoryMock.findRentsByEmployeeId(anyLong())).thenReturn(Collections.singletonList(rent));
//        when(returnRepositoryMock.findReturnalsByEmployeeId(anyLong())).thenReturn(Collections.singletonList(returnal));
//        when(rentRepositoryMock.save(any(Rent.class))).thenReturn(null);
//        when(returnRepositoryMock.save(any(Returnal.class))).thenReturn(null);
//        doNothing().when(employeeRepositoryMock).deleteById(anyLong());
//
//        //when
//        employeeService.deleteEmployee(1L);
//
//        //then
//        verify(employeeRepositoryMock, times(1)).deleteById(1L);
//
//    }
//
//    @Test
//    void shouldNotDeleteEmployee() {
//        //given
//        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
//
//        //when
//        ThrowableAssert.ThrowingCallable callable = () -> employeeService.deleteEmployee(1L);
//
//        //then
//        assertThatThrownBy(callable)
//                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
//                .hasMessage("No employee under that ID!");
//        verify(rentRepositoryMock, never()).save(any(Rent.class));
//        verify(returnRepositoryMock, never()).save(any(Returnal.class));
//        verify(employeeRepositoryMock, never()).deleteById(anyLong());
//    }
//}