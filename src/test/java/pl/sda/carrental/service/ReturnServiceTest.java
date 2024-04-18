package pl.sda.carrental.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sda.carrental.configuration.auth.entity.Employee;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.exceptionHandling.ReturnAlreadyExistsForReservationException;
import pl.sda.carrental.model.*;
import pl.sda.carrental.model.DTO.ReturnDTO;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.repository.ReservationRepository;
import pl.sda.carrental.repository.ReturnRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ReturnServiceTest {
    private final ReturnRepository returnRepositoryMock = mock(ReturnRepository.class);
    private final ReservationRepository reservationRepositoryMock = mock(ReservationRepository.class);
    private final EmployeeRepository employeeRepositoryMock = mock(EmployeeRepository.class);
    private final RevenueService revenueService = mock(RevenueService.class);

    private final ReturnService returnService = new ReturnService(reservationRepositoryMock, returnRepositoryMock,
            employeeRepositoryMock, revenueService);

    private Returnal returnal;
    private Employee employee;
    private Reservation reservation;
    private ReturnDTO returnDTO;

    @BeforeEach
    void setUp() {
        returnal = new Returnal().withReturnId(1L);
        employee = new Employee();
        reservation = new Reservation()
                .withCar(new Car()
                        .withBranch(new Branch()
                                .withRevenue(new Revenue()
                                        .withRevenueId(1L))));
        returnDTO = new ReturnDTO(1L, "comments",
                LocalDate.of(2024, 10, 10),
                new BigDecimal("100.0"), 1L);
    }

    @Test
    void shouldGetAllReturnals() {
        //given
        List<Returnal> returnals = Collections.singletonList(returnal);
        when(returnRepositoryMock.findAll()).thenReturn(returnals);

        //when
        List<Returnal> allReturnals = returnService.getAllReturnals();

        //then
        assertThat(allReturnals)
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(returnal);
    }

    @Test
    void shouldGetAllReturnalsWhenListIsEmpty() {
        //given
        when(returnRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Returnal> allRents = returnService.getAllReturnals();

        //then
        assertThat(allRents)
                .isEmpty();
    }

    @Test
    void shouldSaveRent() {
        //given
        when(returnRepositoryMock.findReturnsWithReservationId(anyLong())).thenReturn(new ArrayList<>());
        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));
        when(reservationRepositoryMock.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(returnRepositoryMock.save(any(Returnal.class))).thenReturn(new Returnal(1L, "comments",
                LocalDate.of(2024, 10, 10), new BigDecimal("20.0"), employee, reservation));
        doNothing().when(revenueService).updateRevenue(anyLong(), any(BigDecimal.class));

        //when
        Returnal savedReturnal = returnService.saveReturn(returnDTO);

        //then
        assertThat(savedReturnal)
                .isNotNull()
                .isInstanceOf(Returnal.class);
        assertThat(savedReturnal.getReturnId()).isEqualTo(1L);
        assertThat(savedReturnal.getComments()).isEqualTo("comments");
        assertThat(savedReturnal.getReturnDate()).isEqualTo(LocalDate.of(2024, 10, 10));
        assertThat(savedReturnal.getReservation()).isEqualTo(reservation);
        assertThat(savedReturnal.getEmployee()).isEqualTo(employee);
        assertThat(savedReturnal.getUpcharge()).isEqualTo(BigDecimal.valueOf(20.0));
        verify(revenueService, times(1)).updateRevenue(1L, BigDecimal.valueOf(100.0));
    }

    @Test
    void shouldNotSaveReturnalWhenAtLeastOneReservationWithRentExists() {
        //given
        when(returnRepositoryMock.findReturnsWithReservationId(anyLong())).thenReturn(List.of(1L));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> returnService.saveReturn(returnDTO);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ReturnAlreadyExistsForReservationException.class)
                .hasMessage("Return already exists for reservation with ID #1");
        verify(returnRepositoryMock, never()).save(any(Returnal.class));
    }

    @Test
    void shouldNotSaveReturnalWhenEmployeeDoesNotExist() {
        //given
        when(returnRepositoryMock.findReturnsWithReservationId(anyLong())).thenReturn(new ArrayList<>());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> returnService.saveReturn(returnDTO);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No employee under ID #1");
        verify(returnRepositoryMock, never()).save(any(Returnal.class));
    }

    @Test
    void shouldNotSaveReturnalWhenReservationDoesNotExist() {
        //given
        when(returnRepositoryMock.findReturnsWithReservationId(anyLong())).thenReturn(new ArrayList<>());
        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> returnService.saveReturn(returnDTO);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("Reservation with ID #1 not found");
        verify(returnRepositoryMock, never()).save(any(Returnal.class));
    }

    @Test
    void shouldEditRent() {
        //given
        when(returnRepositoryMock.findById(anyLong())).thenReturn(Optional.of(returnal));
        when(returnRepositoryMock.findReturnsWithReservationId(anyLong())).thenReturn(new ArrayList<>());
        when(employeeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(employee));
        when(reservationRepositoryMock.findById(anyLong())).thenReturn(Optional.of(reservation));
        when(returnRepositoryMock.save(any(Returnal.class))).thenReturn(returnal);
        doNothing().when(revenueService).updateRevenue(anyLong(), any(BigDecimal.class));

        //when
        Returnal editedReturnal = returnService.editReturnal(1L, returnDTO);

        //then
        assertThat(editedReturnal)
                .isNotNull()
                .isInstanceOf(Returnal.class);
        assertThat(editedReturnal.getReturnId()).isEqualTo(1L);
        assertThat(editedReturnal.getComments()).isEqualTo("comments");
        assertThat(editedReturnal.getReturnDate()).isEqualTo(LocalDate.of(2024, 10, 10));
        assertThat(editedReturnal.getReservation()).isEqualTo(reservation);
        assertThat(editedReturnal.getEmployee()).isEqualTo(employee);
        verify(revenueService, times(1)).updateRevenue(1L, BigDecimal.valueOf(100.0));
    }

    @Test
    void shouldDeleteRentById() {
        //given
        when(returnRepositoryMock.findById(anyLong())).thenReturn(Optional.of(returnal));
        doNothing().when(returnRepositoryMock).deleteById(anyLong());

        //when
        returnService.deleteReturnalById(1L);

        //then
        verify(returnRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotDeleteRentById() {
        //given
        when(returnRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> returnService.deleteReturnalById(1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No returnal under ID #1");
        verify(returnRepositoryMock, never()).deleteById(anyLong());
    }
}