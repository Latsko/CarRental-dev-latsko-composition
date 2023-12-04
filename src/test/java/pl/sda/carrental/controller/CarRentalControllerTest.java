package pl.sda.carrental.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.sda.carrental.model.BranchModel;
import pl.sda.carrental.model.CarRentalModel;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CarRentalControllerTest {

    @Autowired
    private WebTestClient testClient;

    @Test
    void shouldSaveCarRental() {
        Set<BranchModel> branches = new HashSet<>();
        branches.add(new BranchModel(1L, "Radom", " inne", new HashSet<>(), null));

        CarRentalModel carRental = new CarRentalModel(
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