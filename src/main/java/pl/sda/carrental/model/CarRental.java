package pl.sda.carrental.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(exclude = "branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car_rental")
@With
public class CarRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_rental_id")
    private Long carRentalId;
    @NotNull(message = "name field cannot be null")
    private String name;
    @NotNull(message = "Internet Domain field cannot be null")
    private String domain;
    private String address;
    private String owner;
    private String logo;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @OneToMany(mappedBy = "carRental", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Branch> branches = new HashSet<>();
}
