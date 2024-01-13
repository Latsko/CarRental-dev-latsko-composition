package pl.sda.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    private Long employee_id;// fixMe: tu powinien być 'camelCase' a nad nazwą pola powinna być nadana nazwa jako 'snakeCase'
    private String name;
    private String surname;
    private Position position;

    @ManyToOne
    @JoinColumn(name = "branch_id")
//    @JsonBackReference(value = "employee-reference") // wydaje się nadmiarowe
    private Branch branch;
}
