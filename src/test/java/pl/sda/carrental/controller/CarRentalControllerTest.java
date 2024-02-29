package pl.sda.carrental.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.CarRental;
import pl.sda.carrental.service.CarRentalService;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CarRentalControllerTest {
    @Autowired
    private WebTestClient testClient;
    @MockBean
    private CarRentalService service;
    private CarRental carRental;
    private Branch branch;
    private Set<Branch> branches;

    @BeforeEach
    void setUp() {
        branch = new Branch(1L, "Radom", "inne", null,
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                null, null);
        branches = new HashSet<>();
        branches.add(branch);
        carRental = new CarRental(
                1L,
                "Car Rental",
                "www.cars.pl",
                "Warszawa",
                "Janusz",
                "Logo",
                branches
        );
    }

    @Test
    void shouldGetCarRental() {
        Mockito.when(service.getCarRental()).thenReturn(carRental);

        Flux<CarRental> responseBody = testClient.get().uri("/carRental")
                .exchange()
                .expectStatus().isOk()
                .returnResult(CarRental.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new CarRental(
                        1L,
                        "Car Rental",
                        "www.cars.pl",
                        "Warszawa",
                        "Janusz",
                        "Logo",
                        branches
                ))
                .verifyComplete();
    }

    @Test
    void shouldSaveCarRental() {
        Mockito.when(service.saveCarRental(any(CarRental.class))).thenReturn(carRental);

        Flux<CarRental> responseBody = testClient
                .post()
                .uri("/carRental")
                .bodyValue(carRental)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(CarRental.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new CarRental(
                        1L,
                        "Car Rental",
                        "www.cars.pl",
                        "Warszawa",
                        "Janusz",
                        "Logo",
                        branches
                ))
                .verifyComplete();
    }

    @Test
    void shouldOpenBranch() {
        Mockito.when(service.openNewBranch(any(Branch.class))).thenReturn(branch);

        Flux<Branch> responseBody = testClient
                .post()
                .uri("/carRental/addBranch")
                .bodyValue(branch)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Branch.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new Branch(1L, "Radom", "inne", null,
                        new HashSet<>(), new HashSet<>(), new HashSet<>(),
                        null, null))
                .verifyComplete();
    }

    @Test
    void shouldEditCarRental() {
        Mockito.when(service.editCarRental(any(CarRental.class))).thenReturn(carRental);

        Flux<CarRental> responseBody = testClient
                .put()
                .uri("/carRental")
                .bodyValue(carRental)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(CarRental.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new CarRental(
                        1L,
                        "Car Rental",
                        "www.cars.pl",
                        "Warszawa",
                        "Janusz",
                        "Logo",
                        branches
                ))
                .verifyComplete();
    }

    @Test
    void shouldDeleteCarRental() {
        testClient
                .delete()
                .uri("/carRental")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void shouldCloseBranch() {
        Long resourceId = 1L;
        testClient
                .delete()
                .uri("/carRental/deleteBranch/{id}",resourceId)
                .exchange()
                .expectStatus()
                .isOk();
    }

}