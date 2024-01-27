package pl.sda.carrental.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.*;
import pl.sda.carrental.repository.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    private CarRental carRental;
    private Branch branch;
    private Branch branchWithData;
    private Employee manager;
    private List<Reservation> reservations;


    @BeforeEach
    public void setUp() {
        carRental = new CarRental();
        branch = new Branch();
        branchWithData = new Branch(111L, "name", "address", 1L, new HashSet<>(), new HashSet<>(), new HashSet<>(), carRental, new Revenue());
        manager = new Employee(1L, "", "", null, null);
        reservations  = new ArrayList<>() {
            {
                add(new Reservation());
                add(new Reservation());
                add(new Reservation());
            }
        };
        for(Reservation reservation : reservations) {
          reservation.setStartBranch(branchWithData);
          reservation.setEndBranch(branchWithData);
        }
//        reservations.forEach(reservation -> reservation.setStartBranch(branchWithData));
//        reservations.forEach(reservation -> reservation.setEndBranch(branchWithData));
        reservations.add(new Reservation());
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

    @Test
    void shouldGetAllBranches() {
        //given
        given(branchRepositoryMock.findAll()).willReturn(List.of(branch, branch, branch));

        //when
        List<Branch> allBranches = branchService.getAllBranches();

        //then
        assertThat(allBranches).isNotNull();
        assertThat(allBranches).isNotEmpty();
        assertThat(allBranches.size()).isEqualTo(3);
    }

    @Test
    void shouldGetAllBranchesWithEmptyList() {
        //given
        given(branchRepositoryMock.findAll()).willReturn(new ArrayList<>());

        //when
        List<Branch> allBranches = branchService.getAllBranches();

        //then
        assertThat(allBranches).isNotNull();
        assertThat(allBranches.size()).isEqualTo(0);
        assertThat(allBranches).isEmpty();
    }

    @Test
    void shouldEditBranchById() {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branch));
        given(branchRepositoryMock.save(branch)).willReturn(branch);

        //when
        Branch modified = branchService.editBranch(1L, branchWithData);

        //then
        assertThat(modified).isNotNull();
        assertThat(modified.getName()).isEqualTo("name");
        assertThat(modified.getAddress()).isEqualTo("address");
    }

    @Test
    void shouldGetBranchById() {
        //given
        branch.setBranchId(1L);
        given(branchRepositoryMock.findById(1L)).willReturn(Optional.of(branch));

        //when
        Branch foundBranch = branchService.getById(1L);

        //then
        assertThat(foundBranch).isNotNull();
        assertThat(foundBranch.getBranchId()).isEqualTo(1L);
    }

    @Test
    void shouldNotGetBranchById() {
        //given
        //when
        ThrowableAssert.ThrowingCallable callable = () -> branchService.getById(1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No branch under ID #1");
    }

    @Test
    void shouldRemoveBranchWithAllAssociatedReservations() {
        //given
        given(branchRepositoryMock.findById(anyLong())).willReturn(Optional.of(branchWithData));
        given(reservationRepositoryMock.findAll()).willReturn(reservations);

        //when
        branchService.removeBranch(1L);

        //then
        verify(reservationRepositoryMock, times(1)).deleteAll(reservations);
        verify(branchRepositoryMock, times(1)).deleteById(1L);
    }

}