package pl.sda.carrental.configuration.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.sda.carrental.model.Branch;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@With
@Table(name = "clients")
public class Client extends User {
    @NotNull(message = "email cannot be null")
    private String email;
    private String address;

    public Client(final Long id,
                  final String login,
                  final String password,
                  final String name,
                  final String surname,
                  final Branch branch,
                  final List<Role> roles,
                  final String email,
                  final String address) {
        super(id, login, password, name, surname, branch, roles);
        this.email = email;
        this.address = address;
    }
}
