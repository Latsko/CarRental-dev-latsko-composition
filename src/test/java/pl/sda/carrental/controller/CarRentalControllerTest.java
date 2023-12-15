package pl.sda.carrental.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.CarRental;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CarRentalControllerTest {

    @Autowired
    private WebTestClient testClient;

    @Test
    void shouldSaveCarRental() {
        Set<Branch> branches = new HashSet<>();
        branches.add(new Branch(
                                1L,
                                "Radom",
                                " inne",
                                new HashSet<>(),
                                new HashSet<>(),
                                new HashSet<>(),
                                null));

        CarRental carRental = new CarRental(
                null,
                "Car Rental",
                "www.cars.pl",
                "Warszawa",
                "Janusz",
                "Logo",
                branches
                );
        testClient
                //given
                .post()
                .uri("/carRental")
                .bodyValue(carRental)

                //when
                .exchange()
                //then
                .expectStatus()
                .isOk();

    }

}