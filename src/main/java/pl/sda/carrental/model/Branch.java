package pl.sda.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import pl.sda.carrental.configuration.auth.entity.Client;
import pl.sda.carrental.configuration.auth.entity.Employee;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Long branchId;
    private String name;
    private String address;
    @Column(name = "manager_id")
    private Long managerId;
    
    @OneToMany(mappedBy = "branch", orphanRemoval = true)
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "branch", orphanRemoval = true)
    private Set<Car> cars = new HashSet<>();

    @OneToMany(mappedBy = "branch", orphanRemoval = true)
    private Set<Client> clients = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "car_rental_id", nullable = false)
    @JsonBackReference(value = "carRental-reference")
    private CarRental carRental;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "revenue_id")
    private Revenue revenue;
}
