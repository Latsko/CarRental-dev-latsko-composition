package pl.sda.carrental.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.CarRental;
import pl.sda.carrental.model.Employee;
import pl.sda.carrental.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.when;

//@ExtendWith(MockitoExtension.class)
class BranchServiceTest {
    //@Mock
    private BranchRepository branchRepositoryMock = mock(BranchRepository.class);
    //@Mock
    private CarRepository carRepositoryMock = mock(CarRepository.class);
    //@Mock
    private EmployeeRepository employeeRepositoryMock = mock(EmployeeRepository.class);
    //@Mock
    private ReservationRepository reservationRepositoryMock = mock(ReservationRepository.class);
    //@Mock
    private CarRentalRepository carRentalRepositoryMock = mock(CarRentalRepository.class);

    //@InjectMocks
    private BranchService branchService = new BranchService(branchRepositoryMock, carRepositoryMock, employeeRepositoryMock, reservationRepositoryMock, carRentalRepositoryMock);

    private Branch branch;
    private CarRental carRental;
    private Employee manager;


    @BeforeEach
    public void setUp() {
        branch = new Branch();
        carRental = new CarRental();
        manager = new Employee(1L, "", "", null, null);

    }

    @Test
    void shouldSaveBranchWithoutManager() {
        //given
        given(branchRepositoryMock.save(branch)).willReturn(branch);
        given(carRentalRepositoryMock.findAll()).willReturn(List.of(carRental));

        //when
        Branch savedBranch = branchService.addBranch(branch);

        //then
        assertThat(savedBranch).isNotNull();
        verify(branchRepositoryMock, never()).findById(anyLong());
        verify(carRentalRepositoryMock, times(2)).findAll();
    }

    @Test
    void shouldSaveBranchWithManager() {
        //given
        branch.setManagerId(1L);
        given(branchRepositoryMock.save(branch)).willReturn(branch);
        given(employeeRepositoryMock.findById(anyLong())).willReturn(Optional.of(manager));
        given(carRentalRepositoryMock.findAll()).willReturn(List.of(carRental));

        //when
        Branch savedBranch = branchService.addBranch(branch);

        //then
        assertThat(savedBranch).isNotNull();
        assertThat(savedBranch.getManagerId()).isNotNull();
        verify(employeeRepositoryMock, times(1)).findById(anyLong());
        verify(employeeRepositoryMock, times(1)).save(manager);
        verify(carRentalRepositoryMock, times(2)).findAll();
    }

    @Test
    void shouldNotFindCarRentalForBranch() {
        //given
        given(carRentalRepositoryMock.findAll()).willReturn(new ArrayList<>());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> branchService.addBranch(branch);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No Car Rental for branch to be assigned to");
    }

    @Test
    void shouldNotFindManagerForBranch() {
        //given
        given(carRentalRepositoryMock.findAll()).willReturn(List.of(carRental));
        branch.setManagerId(1L);

        //when
        ThrowableAssert.ThrowingCallable callable = () -> branchService.addBranch(branch);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("Cannot find employee to assign as Manager!");
        verify(carRentalRepositoryMock, times(2)).findAll();
        verify(employeeRepositoryMock, never()).save(manager);
        verify(branchRepositoryMock, never()).save(branch);
    }
}