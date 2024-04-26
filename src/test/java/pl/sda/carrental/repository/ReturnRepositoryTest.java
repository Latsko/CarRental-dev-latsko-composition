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
import pl.sda.carrental.model.Reservation;
import pl.sda.carrental.model.Returnal;

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
class ReturnRepositoryTest {
    @Autowired
    private ReturnRepository returnalRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EmployeeRepository employeeRepository;


    private Returnal returnal;

    @BeforeEach
    public void setUp() {
        returnal = new Returnal()
                .withReturnDate(LocalDate.of(2024, 12, 12))
                .withComments("comments");
    }


    @Test
    public void shouldFindReturnalById() {
        //given
        returnalRepository.save(returnal);

        //when
        Returnal found = returnalRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getReturnId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveReturnal() {
        //given

        //when
        Returnal saved = returnalRepository.save(returnal);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getReturnId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllReturnals() {
        //given
        Returnal rent1 = new Returnal()
                .withReturnDate(LocalDate.of(2024, 1, 1))
                .withComments("comment1");
        Returnal rent2 = new Returnal()
                .withReturnDate(LocalDate.of(2024, 2, 2))
                .withComments("comment2");

        returnalRepository.save(rent1);
        returnalRepository.save(rent2);

        //when
        List<Returnal> list = returnalRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateReturnal() {
        //given
        returnalRepository.save(returnal);

        //when
        Returnal saved = returnalRepository.findById(returnal.getReturnId()).get();
        saved.setReturnDate(LocalDate.of(2024, 3, 3));
        saved.setComments("newComments");
        Returnal updated = returnalRepository.save(saved);

        //then
        assertThat(updated.getReturnDate()).isEqualTo(LocalDate.of(2024, 3, 3));
        assertThat(updated.getComments()).isEqualTo("newComments");
    }

    @Test
    public void shouldDeleteReturnal() {
        //given
        returnalRepository.save(returnal);

        //when
        returnalRepository.deleteById(returnal.getReturnId());
        Optional<Returnal> empty = returnalRepository.findById(returnal.getReturnId());

        //then
        assertThat(empty).isEmpty();
    }

    @Test
    public void findReturnsWithReservationId() {
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
        Returnal returnal1 = new Returnal().withReservation(reservation1);
        returnalRepository.save(returnal1);

        //when
        List<Long> returnsWithReservationId = returnalRepository.findReturnsWithReservationId(1L);

        //then
        assertThat(returnsWithReservationId).isNotEmpty();
        assertThat(returnsWithReservationId.size()).isEqualTo(1);
        assertThat(returnsWithReservationId.get(0)).isEqualTo(1L);
    }

    @Test
    public void findReturnalsByEmployeeId() {
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
        Returnal returnal1 = new Returnal()
                .withReservation(reservation1)
                .withEmployee(employee1);
        returnalRepository.save(returnal1);

        //when
        List<Returnal> returnalsByEmployeeId = returnalRepository.findReturnalsByEmployeeId(1L);

        //then
        assertThat(returnalsByEmployeeId).isNotEmpty();
        assertThat(returnalsByEmployeeId.size()).isEqualTo(1);
        assertThat(returnalsByEmployeeId.get(0)).isEqualTo(returnal1);
    }
}