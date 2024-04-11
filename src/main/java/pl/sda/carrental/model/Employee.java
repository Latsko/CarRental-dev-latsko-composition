package pl.sda.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.sda.carrental.model.enums.Position;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;
    @NotNull(message = "name cannot be null")
    private String name;
    @NotNull(message = "surname cannot be null")
    private String surname;
    private Position position;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    @JsonBackReference(value = "employee-reference")
    private Branch branch;
}
