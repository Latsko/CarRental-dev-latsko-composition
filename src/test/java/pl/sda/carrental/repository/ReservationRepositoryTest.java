package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.configuration.auth.model.Client;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Reservation reservation;
    private Car car;
    private Client client;

    @BeforeEach
    public void setUp() {
        car = new Car();
        carRepository.save(car);
        client = new Client(1L,
                "login",
                "password",
                "name",
                "surname",
                null,
                null,
                "email@gmail.com",
                null);
        clientRepository.save(client);
        reservation = new Reservation()
                .withPrice(BigDecimal.valueOf(100.0))
                .withStartDate(LocalDate.of(2024, 1, 1))
                .withEndDate(LocalDate.of(2024, 1, 5))
                .withCar(car)
                .withClient(client);
    }


    @Test
    public void shouldFindReservationById() {
        //given
        reservationRepository.save(reservation);

        //when
        Reservation found = reservationRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getReservationId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveReservation() {
        //given

        //when
        Reservation saved = reservationRepository.save(reservation);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getReservationId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllBReservations() {
        //given
        Reservation reservation1 = new Reservation()
                .withPrice(BigDecimal.valueOf(200.0))
                .withStartDate(LocalDate.of(2024, 2, 1))
                .withEndDate(LocalDate.of(2024, 2, 5))
                .withCar(car)
                .withClient(client);
        Reservation reservation2 = new Reservation()
                .withPrice(BigDecimal.valueOf(300.0))
                .withStartDate(LocalDate.of(2024, 3, 1))
                .withEndDate(LocalDate.of(2024, 3, 5))
                .withCar(car)
                .withClient(client);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        //when
        List<Reservation> list = reservationRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateReservation() {
        //given
        reservationRepository.save(reservation);

        //when
        Reservation saved = reservationRepository.findById(reservation.getReservationId()).get();
        saved.setPrice(BigDecimal.valueOf(350.0));
        saved.setEndDate(LocalDate.of(2024, 2, 7));
        Reservation updated = reservationRepository.save(saved);

        //then
        assertThat(updated.getPrice()).isEqualTo(BigDecimal.valueOf(350.0));
        assertThat(updated.getEndDate()).isEqualTo(LocalDate.of(2024, 2, 7));
    }

    @Test
    public void shouldDeleteReservation() {
        //given
        reservationRepository.save(reservation);

        //when
        reservationRepository.deleteById(reservation.getReservationId());
        Optional<Reservation> empty = reservationRepository.findById(reservation.getReservationId());

        //then
        assertThat(empty).isEmpty();
    }
}