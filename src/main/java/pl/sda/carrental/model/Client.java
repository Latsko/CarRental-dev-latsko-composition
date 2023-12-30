package pl.sda.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long client_id;
    @NotNull(message = "name cannot be null")
    private String name;
    @NotNull(message = "surname cannot be null")
    private String surname;
    @NotNull(message = "email cannot be null")
    private String email;
    @NotNull(message = "address cannot be null")
    private String address;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference(value = "client-reference")
    private Branch branch;
}
