package pl.sda.carrental.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sda.carrental.model.*;
import pl.sda.carrental.model.DTO.ReservationDTO;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationServiceTest {

    @Mock
    private CarRepository carRepositoryMock;
    @Mock
    private ClientRepository clientRepositoryMock;
    @Mock
    private BranchRepository branchRepositoryMock;
    @Mock
    private ReservationRepository reservationRepositoryMock;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldGetAllReservations() {
        // given
        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);

        when(reservation1.getClient()).thenReturn(new Client());
        when(reservation1.getCar()).thenReturn(new Car());
        when(reservation1.getStartBranch()).thenReturn(new Branch());
        when(reservation1.getEndBranch()).thenReturn(new Branch());

        when(reservation2.getClient()).thenReturn(new Client());
        when(reservation2.getCar()).thenReturn(new Car());
        when(reservation2.getStartBranch()).thenReturn(new Branch());
        when(reservation2.getEndBranch()).thenReturn(new Branch());

        when(reservationRepositoryMock.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        // when
        List<ReservationDTO> result = reservationService.getAllReservations();

        // then
        assertThat(2).isEqualTo(result.size());
    }

    @Test
    void shouldSaveReservation() {
        //given
        ReservationDTO reservationDto = new ReservationDTO(
                1L,
                1L,
                1L,
                LocalDate.of(2023, 11, 20),
                LocalDate.of(2023, 11, 22),
                1L,
                1L,
                null,
                null
        );

        Revenue revenue = new Revenue(1L, new BigDecimal("10000"));

        Branch branch = new Branch(
                1L,
                "Warszawa",
                "ul. Przykladowa",
                null,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                null,
                revenue);

        Car car = new Car(
                1L,
                "Kia",
                "Ceed",
                "Sedan",
                2002,
                "Gray",
                20000,
                Status.AVAILABLE,
                BigDecimal.valueOf(100),
                branch,
                new HashSet<>()
        );

        Client client = new Client(1L,
                "name",
                "surname",
                "email",
                "address",
                branch);

        Mockito.when(branchRepositoryMock.findById(1L)).thenReturn(Optional.of(branch));
        Mockito.when(carRepositoryMock.findById(1L)).thenReturn(Optional.of(car));
        Mockito.when(clientRepositoryMock.findById(1L)).thenReturn(Optional.of(client));

        //when
        reservationService.saveReservation(reservationDto);

        //then
        Mockito.verify(carRepositoryMock).findById(1L);
        Mockito.verify(branchRepositoryMock, times(2)).findById(1L);
        Mockito.verify(clientRepositoryMock).findById(1L);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        Mockito.verify(reservationRepositoryMock).save(captor.capture());
        Reservation result = captor.getValue();

        assertThat(result.getEndDate()).isEqualTo("2023-11-22");
        assertThat(result.getStartDate()).isEqualTo("2023-11-20");
        assertThat(result.getPrice().intValue()).isEqualTo(200);
    }
}