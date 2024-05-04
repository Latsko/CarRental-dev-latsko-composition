package pl.sda.carrental;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Car Rental REST API", version = "1.0.0",
		summary = "Spring REST API for a very simplified Car Rental model",
		description = """
                <h2>This API allows to manage a car rental and make car reservations for clients.</h2>\
                <p>There are 3 levels of management: admin, manager and employee.</p>\
                <ul>\
                <li>Admin - can manage car rental, branches, revenues, remove and assign managers. (/api/admin/&ast;&ast;)</li>\
                <li>Manager - manages cars and employees. (/api/manageL1/&ast;&ast;)</li>\
                <li>Employee - can manage rents and returnals, process cars. (/api/manageL2/&ast;&ast;)</li>\
                </ul>\
                <p>Clients can make multiple reservations for multiple cars, rent and return them. \
                Car cannot be reserved if there are time collisions with other reservations or if \
                returnal branch of the previous reservation differs from the rent branch of the new \
                reservation and there is less than one day for car to get to it's destination branch to be rented.</p>\
                <h2>User Credentials</h2>

                <table>
                    <thead>
                        <tr>
                            <th>Login</th>
                            <th>Password</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>admin</td>
                            <td>Apassword</td>
                        </tr>
                        <tr>
                            <td>manager</td>
                            <td>Mpassword</td>
                        </tr>
                        <tr>
                            <td>employee</td>
                            <td>Epassword</td>
                        </tr>
                        <tr>
                            <td>client</td>
                            <td>Cpassword</td>
                        </tr>
                    </tbody>
                </table>"""))
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
