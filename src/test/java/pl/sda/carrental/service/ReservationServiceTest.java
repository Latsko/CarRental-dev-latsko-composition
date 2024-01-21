package pl.sda.carrental.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sda.carrental.model.*;
import pl.sda.carrental.model.DTO.ReservationDTO;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.repository.ClientRepository;
import pl.sda.carrental.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

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

        Branch branch = new Branch(
                1L,
                "Warszawa",
                "ul. Przykladowa",
                null,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                null,
                null);

        Mockito.when(branchRepositoryMock.findById(1L)).thenReturn(Optional.of(branch));
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
                null,
                new HashSet<>()
        );
        Mockito.when(carRepositoryMock.findById(1L)).thenReturn(Optional.of(car));

        Client client = new Client(1L,
                "name",
                "surname",
                "email",
                "address",
                null);

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