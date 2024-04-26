package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.model.CarRental;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CarRentalRepositoryTest {

    @Autowired
    private CarRentalRepository carRentalRepository;

    private CarRental carRental;
    
    @BeforeEach
    public void setUp() {
        carRental = new CarRental(null,
                "rentalName",
                "rentalDomain.com",
                "address",
                "owner",
                "logo",
                new HashSet<>());
    }

    @Test
    public void shouldFindCarRentalById() {
        //given
        carRentalRepository.save(carRental);

        //when
        CarRental found = carRentalRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getCarRentalId()).isEqualTo(1L);
    }

    @Test
    public void shouldFindAllCarRentals() {
        //given
        carRentalRepository.save(carRental);

        //when
        List<CarRental> carRentals = carRentalRepository.findAll();

        //then
        assertThat(carRentals).isNotEmpty();
        assertThat(carRentals).hasSize(1);
        assertThat(carRentals.stream().findFirst().get().getCarRentalId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveCarRental() {
        //given


        //when
        CarRental saved = carRentalRepository.save(carRental);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getCarRentalId()).isGreaterThan(0);
    }

    @Test
    public void shouldUpdateCarRental() {
        //given
        carRentalRepository.save(carRental);

        //when
        CarRental saved = carRentalRepository.findById(carRental.getCarRentalId()).get();
        saved.setName("newName");
        saved.setOwner("newOwner");
        CarRental updated = carRentalRepository.save(saved);

        //then
        assertThat(updated.getName()).isEqualTo("newName");
        assertThat(updated.getOwner()).isEqualTo("newOwner");
    }
    
    @Test
    public void shouldDeleteCarRental() {
        //given
        carRentalRepository.save(carRental);
            
        //when
        carRentalRepository.deleteById(1L);
        Optional<CarRental> empty = carRentalRepository.findById(1L);

        //then
        assertThat(empty).isEmpty();
    }
}