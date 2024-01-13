package pl.sda.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "branch")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branch_id;
    private String name;
    private String address;
    
    @OneToMany(mappedBy = "branch", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Car> cars = new HashSet<>();

    @OneToMany(mappedBy = "branch", cascade = CascadeType.REMOVE, orphanRemoval = true)
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
