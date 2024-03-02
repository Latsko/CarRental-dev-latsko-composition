package pl.sda.carrental.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.sda.carrental.exceptionHandling.ObjectNotFoundInRepositoryException;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.Car;
import pl.sda.carrental.model.Reservation;
import pl.sda.carrental.model.enums.Status;
import pl.sda.carrental.repository.BranchRepository;
import pl.sda.carrental.repository.CarRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CarServiceTest {
    private final CarRepository carRepositoryMock = mock(CarRepository.class);
    private final BranchRepository branchRepositoryMock = mock(BranchRepository.class);
    private final CarService carService = new CarService(carRepositoryMock, branchRepositoryMock);
    private Branch branch;
    private Car car;

    @BeforeEach
    void setUp() {
        branch = new Branch(1L, "name", "address", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(), null, null);
        car = new Car(1L, "make", "model", "bodyStyle",
                2000, "colour", 10000.0, Status.AVAILABLE,
                new BigDecimal("100.0"), branch, new HashSet<>());
    }

    @Test
    void shouldGetCarById() {
        //given
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));

        //when
        Car carById = carService.getCarById(1L);

        //then
        assertThat(carById)
                .isNotNull()
                .isInstanceOf(Car.class);
        assertThat(carById.getCarId()).isEqualTo(1L);
        assertThat(carById.getBranch()).isEqualTo(branch);
        assertThat(carById.getMake()).isEqualTo("make");
        assertThat(carById.getModel()).isEqualTo("model");
        assertThat(carById.getBodyStyle()).isEqualTo("bodyStyle");
        assertThat(carById.getYear()).isEqualTo(2000);
        assertThat(carById.getColour()).isEqualTo("colour");
        assertThat(carById.getMileage()).isEqualTo(10000.0);
        assertThat(carById.getPrice()).isEqualTo(new BigDecimal("100.0"));
    }

    @Test
    void shouldNotGetCarById() {
        //given
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> carService.getCarById(1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("There is no car with selected id");
    }

    @Test
    void shouldGetCarsWithNonEmptyList() {
        //given
        List<Car> cars = Collections.singletonList(car);
        when(carRepositoryMock.findAll()).thenReturn(cars);

        //when
        List<Car> allCars = carService.getCars();

        //then
        assertThat(allCars)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .containsExactly(car);

    }

    @Test
    void shouldGetCarsWithEmptyList() {
        //given
        when(carRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        //when
        List<Car> allCars = carService.getCars();

        //then
        assertThat(allCars)
                .isEmpty();
    }


    @Test
    void shouldAddCar() {
        //given
        when(carRepositoryMock.save(any(Car.class))).thenReturn(car);

        //when
        Car savedCar = carService.addCar(car);

        //then
        assertThat(savedCar)
                .isNotNull()
                .isInstanceOf(Car.class);
        assertThat(savedCar.getCarId()).isEqualTo(1L);
        assertThat(savedCar.getBranch()).isEqualTo(branch);
        assertThat(savedCar.getMake()).isEqualTo("make");
        assertThat(savedCar.getModel()).isEqualTo("model");
        assertThat(savedCar.getBodyStyle()).isEqualTo("bodyStyle");
        assertThat(savedCar.getYear()).isEqualTo(2000);
        assertThat(savedCar.getColour()).isEqualTo("colour");
        assertThat(savedCar.getMileage()).isEqualTo(10000.0);
        assertThat(savedCar.getPrice()).isEqualTo(new BigDecimal("100.0"));
    }

    @Test
    void shouldEditCarWithBranch() {
        //given
        branch.getCars().add(car);
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));
        when(branchRepositoryMock.save(any(Branch.class))).thenReturn(branch);
        when(carRepositoryMock.save(any(Car.class))).thenReturn(car);

        //when
        Car editedCar = carService.editCar(1L, new Car()
                .withMake("changedMake")
                .withModel("changedModel")
                .withBodyStyle("changedBodyStyle")
                .withYear(1990)
                .withColour("changedColour")
                .withMileage(12345.6)
                .withPrice(new BigDecimal("222.2")));

        //then
        assertThat(editedCar)
                .isNotNull()
                .isInstanceOf(Car.class);
        assertThat(editedCar.getCarId()).isEqualTo(1L);
        assertThat(editedCar.getBranch()).isEqualTo(branch);
        assertThat(editedCar.getMake()).isEqualTo("changedMake");
        assertThat(editedCar.getModel()).isEqualTo("changedModel");
        assertThat(editedCar.getBodyStyle()).isEqualTo("changedBodyStyle");
        assertThat(editedCar.getYear()).isEqualTo(1990);
        assertThat(editedCar.getColour()).isEqualTo("changedColour");
        assertThat(editedCar.getMileage()).isEqualTo(12345.6);
        assertThat(editedCar.getPrice()).isEqualTo(new BigDecimal("222.2"));
    }

    @Test
    void shouldEditCarWithOutBranch() {
        //given
        car.setBranch(null);
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));
        when(carRepositoryMock.save(any(Car.class))).thenReturn(car);

        //when
        Car editedCar = carService.editCar(1L, new Car()
                .withMake("changedMake")
                .withModel("changedModel")
                .withBodyStyle("changedBodyStyle")
                .withYear(1990)
                .withColour("changedColour")
                .withMileage(12345.6)
                .withPrice(new BigDecimal("222.2")));

        //then
        assertThat(editedCar)
                .isNotNull()
                .isInstanceOf(Car.class);
        assertThat(editedCar.getCarId()).isEqualTo(1L);
        assertThat(editedCar.getBranch()).isNull();
        assertThat(editedCar.getMake()).isEqualTo("changedMake");
        assertThat(editedCar.getModel()).isEqualTo("changedModel");
        assertThat(editedCar.getBodyStyle()).isEqualTo("changedBodyStyle");
        assertThat(editedCar.getYear()).isEqualTo(1990);
        assertThat(editedCar.getColour()).isEqualTo("changedColour");
        assertThat(editedCar.getMileage()).isEqualTo(12345.6);
        assertThat(editedCar.getPrice()).isEqualTo(new BigDecimal("222.2"));
    }

    @Test
    void shouldNotEditCar() {
        //given
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));

        //when
        ThrowableAssert.ThrowingCallable callable = () -> carService.editCar(1L, new Car());

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("No car under ID #1 in that branch");

    }

    @Test
    void shouldDeleteCarById() {
        //given
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));
        doNothing().when(carRepositoryMock).deleteById(anyLong());

        //when
        carService.deleteCarById(1L);

        //then
        verify(carRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void shouldNotDeleteCarById() {
        //given
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        doNothing().when(carRepositoryMock).deleteById(anyLong());

        //when
        ThrowableAssert.ThrowingCallable callable = () -> carService.deleteCarById(1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(ObjectNotFoundInRepositoryException.class)
                .hasMessage("There is no car with selected id");

    }

    @Test
    void shouldUpdateMileageAndPrice() {
        //given
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));
        when(carRepositoryMock.save(any(Car.class))).thenReturn(car);

        //when
        Car updatedCar = carService.updateMileageAndPrice(11111.1, new BigDecimal("321.12"), 1L);

        //then
        assertThat(updatedCar)
                .isNotNull()
                .isInstanceOf(Car.class);
        assertThat(updatedCar.getCarId()).isEqualTo(1L);
        assertThat(updatedCar.getMileage()).isEqualTo(11111.1);
        assertThat(updatedCar.getPrice()).isEqualTo(new BigDecimal("321.12"));

    }

    @Test
    void shouldUpdateStatus() {
        //given
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));
        when(carRepositoryMock.save(any(Car.class))).thenReturn(car);

        //when
        Car changedCar = carService.updateStatus("RENTED", 1L);

        //then
        assertThat(changedCar)
                .isNotNull()
                .isInstanceOf(Car.class);
        assertThat(changedCar.getCarId()).isEqualTo(1L);
        assertThat(changedCar.getStatus()).isEqualTo(Status.RENTED);
    }

    @Test
    void shouldNotUpdateStatus() {
        //given
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));

        //when
        ThrowableAssert.ThrowingCallable callable = () ->
                carService.updateStatus("somethingThatWillCallException", 1L);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Wrong argument for car Status!");
    }

    @Test
    void shouldSetStatusOnDateForCarUnderId() {
        //given
        car.getReservations().add(new Reservation()
                .withStartDate(LocalDate.of(2024, 10, 10))
                .withEndDate(LocalDate.of(2024, 10 ,12)));
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));

        //when
        Status statusOnDateForCarUnderId = carService.getStatusOnDateForCarUnderId(1L, LocalDate.of(2024, 10, 15));

        //then
        assertThat(statusOnDateForCarUnderId).isEqualTo(Status.AVAILABLE);
    }

    @Test
    void shouldMapCarToCarDTO() {
        //given
        car.getReservations().add(new Reservation()
                .withStartDate(LocalDate.of(2024, 10, 10))
                .withEndDate(LocalDate.of(2024, 10 ,12)));
        when(carRepositoryMock.findById(anyLong())).thenReturn(Optional.of(car));

        //when
        Status statusOnDateForCarUnderId = carService.getStatusOnDateForCarUnderId(1L, LocalDate.of(2024, 10, 11));

        //then
        assertThat(statusOnDateForCarUnderId).isEqualTo(Status.RENTED);
    }
}