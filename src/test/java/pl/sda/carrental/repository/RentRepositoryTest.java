package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.configuration.auth.entity.Client;
import pl.sda.carrental.configuration.auth.entity.Employee;
import pl.sda.carrental.configuration.auth.repository.ClientRepository;
import pl.sda.carrental.configuration.auth.repository.EmployeeRepository;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.Rent;
import pl.sda.carrental.model.Reservation;

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
class RentRepositoryTest {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    private Rent rent;

    @BeforeEach
    public void setUp() {
        rent = new Rent()
                .withRentDate(LocalDate.of(2024, 12, 12))
                .withComments("comments");
    }


    @Test
    public void shouldFindRentById() {
        //given
        rentRepository.save(rent);

        //when
        Rent found = rentRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getRentId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveRent() {
        //given

        //when
        Rent saved = rentRepository.save(rent);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getRentId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllRents() {
        //given
        Rent rent1 = new Rent()
                .withRentDate(LocalDate.of(2024, 1, 1))
                .withComments("comment1");
        Rent rent2 = new Rent()
                .withRentDate(LocalDate.of(2024, 2, 2))
                .withComments("comment2");

        rentRepository.save(rent1);
        rentRepository.save(rent2);

        //when
        List<Rent> list = rentRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateRent() {
        //given
        rentRepository.save(rent);

        //when
        Rent saved = rentRepository.findById(rent.getRentId()).get();
        saved.setRentDate(LocalDate.of(2024, 3, 3));
        saved.setComments("newComments");
        Rent updated = rentRepository.save(saved);

        //then
        assertThat(updated.getRentDate()).isEqualTo(LocalDate.of(2024, 3, 3));
        assertThat(updated.getComments()).isEqualTo("newComments");
    }

    @Test
    public void shouldDeleteRent() {
        //given
        rentRepository.save(rent);

        //when
        rentRepository.deleteById(rent.getRentId());
        Optional<Rent> empty = rentRepository.findById(rent.getRentId());

        //then
        assertThat(empty).isEmpty();
    }

    @Test
    void findRentalsWithReservationId() {
        //given
        Client client1 = new Client()
                .withEmail("email1@email.com");
        client1.setLogin("login");
        client1.setPassword("password");
        clientRepository.save(client1);
        Car car1 = new Car();
        carRepository.save(car1);
        Reservation reservation1 = new Reservation()
                .withStartDate(LocalDate.of(2024, 1, 1))
                .withEndDate(LocalDate.of(2024, 2, 2))
                .withCar(car1)
                .withClient(client1);
        reservationRepository.save(reservation1);
        Rent rent1 = new Rent().withReservation(reservation1);
        rentRepository.save(rent1);

        //when
        List<Long> returnsWithReservationId = rentRepository.findRentalsWithReservationId(1L);

        //then
        assertThat(returnsWithReservationId).isNotEmpty();
        assertThat(returnsWithReservationId.size()).isEqualTo(1);
        assertThat(returnsWithReservationId.get(0)).isEqualTo(1L);
    }

    @Test
    void findRentsByEmployeeId() {
        //given
        Employee employee1 = new Employee();
        employee1.setLogin("login1");
        employee1.setPassword("password1");
        employeeRepository.save(employee1);
        Client client1 = new Client()
                .withEmail("email1@email.com");
        client1.setLogin("login2");
        client1.setPassword("password2");
        clientRepository.save(client1);
        Car car1 = new Car();
        carRepository.save(car1);
        Reservation reservation1 = new Reservation()
                .withStartDate(LocalDate.of(2024, 1, 1))
                .withEndDate(LocalDate.of(2024, 2, 2))
                .withCar(car1)
                .withClient(client1);
        reservationRepository.save(reservation1);
        Rent rent1 = new Rent()
                .withReservation(reservation1)
                .withEmployee(employee1);
        rentRepository.save(rent1);

        //when
        List<Rent> returnalsByEmployeeId = rentRepository.findRentsByEmployeeId(1L);

        //then
        assertThat(returnalsByEmployeeId).isNotEmpty();
        assertThat(returnalsByEmployeeId.size()).isEqualTo(1);
        assertThat(returnalsByEmployeeId.get(0)).isEqualTo(rent1);
    }
}