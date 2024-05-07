package pl.sda.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import pl.sda.carrental.configuration.auth.model.Client;
import pl.sda.carrental.configuration.auth.model.Employee;

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

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(mappedBy = "branch", orphanRemoval = true)
    private Set<Employee> employees = new HashSet<>();

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(mappedBy = "branch", orphanRemoval = true)
    private Set<Car> cars = new HashSet<>();

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(mappedBy = "branch", orphanRemoval = true)
    private Set<Client> clients = new HashSet<>();

    @Schema(hidden = true)
    @ManyToOne
    @JoinColumn(name = "car_rental_id", nullable = false)
    @JsonBackReference(value = "carRental-reference")
    private CarRental carRental;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "revenue_id")
    private Revenue revenue;
}
