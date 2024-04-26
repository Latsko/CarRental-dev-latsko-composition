package pl.sda.carrental.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.sda.carrental.model.Car;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    private Car car;

    @BeforeEach
    public void setUp() {
        car = new Car().withModel("model").withColour("color");
    }


    @Test
    public void shouldFindBranchById() {
        //given
        carRepository.save(car);

        //when
        Car found = carRepository.findById(1L).orElse(null);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getCarId()).isEqualTo(1L);
    }

    @Test
    public void shouldSaveBranch() {
        //given

        //when
        Car saved = carRepository.save(car);

        //then
        assertThat(saved).isNotNull();
        assertThat(saved.getCarId()).isGreaterThan(0);
    }

    @Test
    public void shouldFindAllBranches() {
        //given
        Car car1 = new Car().withColour("colour1").withModel("model1");
        Car car2 = new Car().withColour("colour2").withModel("model2");

        carRepository.save(car1);
        carRepository.save(car2);

        //when
        List<Car> list = carRepository.findAll();

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }


    @Test
    public void shouldUpdateBranch() {
        //given
        carRepository.save(car);

        //when
        Car saved = carRepository.findById(car.getCarId()).get();
        saved.setColour("newColour");
        saved.setModel("newModel");
        Car updated = carRepository.save(saved);

        //then
        assertThat(updated.getColour()).isEqualTo("newColour");
        assertThat(updated.getModel()).isEqualTo("newModel");
    }

    @Test
    public void shouldDeleteBranch() {
        //given
        carRepository.save(car);

        //when
        carRepository.deleteById(car.getCarId());
        Optional<Car > empty = carRepository.findById(car.getCarId());

        //then
        assertThat(empty).isEmpty();
    }
}