package pl.sda.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "branch")
public class BranchModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branch_id;
    private String name;
    private String address;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    private Set<EmployeeModel> employees = new HashSet<>();

    @OneToMany(mappedBy = "branch")
    //@JoinColumn(name = "branch_id")
    private Set<CarModel> cars = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "branch_id")
    private Set<ClientModel> clients = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "car_rental_id", nullable = false)
    @JsonBackReference
    private CarRentalModel carRental;
}
