package pl.sda.carrental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.carrental.configuration.security.entity.User;
import pl.sda.carrental.model.enums.Position;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "employee")
public class Employee extends User {
    private Position position;
}
