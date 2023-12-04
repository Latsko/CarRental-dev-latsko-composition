package pl.sda.carrental.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sda.carrental.model.BranchModel;
import pl.sda.carrental.model.CarModel;
import pl.sda.carrental.model.ReservationDTO;
import pl.sda.carrental.model.ReservationModel;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;
import pl.sda.carrental.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservationServiceTest {

    @Mock
    private CarRepository carRepositoryMock;
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
                "Ted",
                1L,
                LocalDate.of(2023, 11, 20),
                LocalDate.of(2023, 11, 22),
                1L,
                1L
        );

        BranchModel branch = new BranchModel(
                1L,
                "Warszawa",
                "ul. Przykladowa",
                new HashSet<>(),
                null);
        Mockito.when(branchRepositoryMock.findById(1L)).thenReturn(Optional.of(branch));
        CarModel car = new CarModel(
                1L,
                "Kia",
                "Ceed",
                "Sedan",
                2002,
                "Gray",
                20000,
                Status.AVAILABLE,
                BigDecimal.valueOf(100)
        );
        Mockito.when(carRepositoryMock.findById(1L)).thenReturn(Optional.of(car));

        //when
        reservationService.saveReservation(reservationDto);

        //then
        Mockito.verify(carRepositoryMock).findById(1L);

        ArgumentCaptor<ReservationModel> captor = ArgumentCaptor.forClass(ReservationModel.class);
        Mockito.verify(reservationRepositoryMock).save(captor.capture());
        ReservationModel result = captor.getValue();

        assertThat(result.getEndDate()).isEqualTo("2023-11-22");
        assertThat(result.getStartDate()).isEqualTo("2023-11-20");
        assertThat(result.getPrice().intValue()).isEqualTo(200);
    }
}