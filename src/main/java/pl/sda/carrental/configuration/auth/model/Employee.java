package pl.sda.carrental.configuration.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.carrental.model.Branch;
import pl.sda.carrental.model.enums.Position;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "employees")
public class Employee extends User {
    private Position position;

    public Employee(final Long id,
                    final String login,
                    final String password,
                    final String name,
                    final String surname,
                    final Branch branch,
                    final List<Role> roles,
                    final Position position) {
        super(id, login, password, name, surname, branch, roles);
        this.position = position;
    }
}
