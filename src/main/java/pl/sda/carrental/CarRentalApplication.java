package pl.sda.carrental;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Car Rental REST API", version = "1.0.0",
description = "Spring REST API for a very simplified Car Rental model"))
@SecurityScheme(
		type = SecuritySchemeType.HTTP,
		name = "basicAuth",
		scheme = "basic"
)
public class CarRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentalApplication.class, args);
	}

}
