package pl.sda.carrental.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.exceptionHandling.RentAlreadyExistsForReservationException;
import pl.sda.carrental.model.DTO.RentDTO;
import pl.sda.carrental.configuration.auth.model.Employee;
import pl.sda.carrental.model.Rent;
import pl.sda.carrental.model.Reservation;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.repository.RentRepository;
import pl.sda.carrental.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RentServiceTest {
    private final RentRepository rentRepositoryMock = mock(RentRepository.class);
    private final ReservationRepository reservationRepositoryMock = mock(ReservationRepository.class);
    private final EmployeeRepository employeeRepositoryMock = mock(EmployeeRepository.class);

    private final RentService rentService = new RentService(rentRepositoryMock, reservationRepositoryMock, employeeRepositoryMock);

    private Rent rent;
    private Employee employee;
    private Reservation reservation;
    private RentDTO rentDTO;


    @BeforeEach
    void setUp() {
        rent = new Rent().withRentId(1L);
        employee = new Employee();
        reservation = new Reservation();
        rentDTO = new RentDTO(1L, "comments",
                LocalDate.of(2024, 10, 10), 1L);
    }

    @Test
    void shouldGetAllRents() {
        //given
        List<Rent> rents = Collections.singletonList(rent);
        when(rentRepositoryMock.findAll()).thenReturn(rents);

        //when
        List<Rent> allRents = rentService.getAllRents();

        //then
        assertThat(allRents)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(rent);
    }

    @Test
    void shouldGetAllRentsWhenListIsEmpty() {
        //given
        when(rentRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Rent> allRents = rentService.getAllRents();

        //then
        assertThat(allRents)
                .isEmpty();
    }

    @Test
    void shouldSaveRent() {
        //given
        when(rentRepositoryMock.findRentalsWithReservationId(anyLong())).thenReturn(new ArrayList<>());
        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));
        when(reservationRepositoryMock.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(rentRepositoryMock.save(any(Rent.class))).thenReturn(new Rent(1L, "comments",
                LocalDate.of(2024, 10, 10), employee, reservation));

        //when
        Rent savedRent = rentService.saveRent(rentDTO);

        //then
        assertThat(savedRent)
                .isNotNull()
                .isInstanceOf(Rent.class);
        assertThat(savedRent.getRentId()).isEqualTo(1L);
        assertThat(savedRent.getComments()).isEqualTo("comments");
        assertThat(savedRent.getRentDate()).isEqualTo(LocalDate.of(2024,10,10));
        assertThat(savedRent.getReservation()).isEqualTo(reservation);
        assertThat(savedRent.getEmployee()).isEqualTo(employee);
    }

    @Test
    void shouldNotSaveRentWhenIsAtLeastOneReservationWithRent() {
        //given
        when(rentRepositoryMock.findRentalsWithReservationId(anyLong())).thenReturn(List.of(1L));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> rentService.saveRent(rentDTO);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(RentAlreadyExistsForReservationException.class)
                .hasMessage("Rent already exists for reservation with ID #1");
        verify(rentRepositoryMock, never()).save(any(Rent.class));
    }

    @Test
    void shouldNotSaveRentWhenEmployeeDoesNotExist() {
        //given
        when(rentRepositoryMock.findRentalsWithReservationId(anyLong())).thenReturn(new ArrayList<>());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> rentService.saveRent(rentDTO);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No employee under ID #1");
        verify(rentRepositoryMock, never()).save(any(Rent.class));
    }

    @Test
    void shouldNotSaveRentWhenReservationDoesNotExist() {
        //given
        when(rentRepositoryMock.findRentalsWithReservationId(anyLong())).thenReturn(new ArrayList<>());
        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> rentService.saveRent(rentDTO);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("Reservation with ID #1 not found");
        verify(rentRepositoryMock, never()).save(any(Rent.class));
    }

    @Test
    void shouldEditRent() {
        //given
        when(rentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(rent));
        when(rentRepositoryMock.findRentalsWithReservationId(anyLong())).thenReturn(new ArrayList<>());
        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));
        when(reservationRepositoryMock.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(rentRepositoryMock.save(any(Rent.class))).thenReturn(rent);

        //when
        Rent editedRent = rentService.editRent(1L, rentDTO);

        //then
        assertThat(editedRent)
                .isNotNull()
                .isInstanceOf(Rent.class);
        assertThat(editedRent.getRentId()).isEqualTo(1L);
        assertThat(editedRent.getComments()).isEqualTo("comments");
        assertThat(editedRent.getRentDate()).isEqualTo(LocalDate.of(2024,10,10));
        assertThat(editedRent.getReservation()).isEqualTo(reservation);
        assertThat(editedRent.getEmployee()).isEqualTo(employee);
    }

    @Test
    void shouldNotEditWhenRentDoesNotExist() {
        //given
        when(rentRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> rentService.editRent(1L, rentDTO);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No rent under ID #1");
        verify(rentRepositoryMock, never()).save(any(Rent.class));
    }

    @Test
    void shouldDeleteRentById() {
        //given
        when(rentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(rent));
        doNothing().when(rentRepositoryMock).deleteById(anyLong());

        //when
        rentService.deleteRentById(1L);

        //then
        verify(rentRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotDeleteRentById() {
        //given
        when(rentRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> rentService.deleteRentById(1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No rent under ID #1");
        verify(rentRepositoryMock, never()).deleteById(anyLong());
    }
}