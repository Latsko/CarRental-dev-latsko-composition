package pl.sda.carrental.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.sda.carrental.configuration.security.entity.User;

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
}
