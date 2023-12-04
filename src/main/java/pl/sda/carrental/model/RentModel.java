package pl.sda.carrental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "rent")
public class RentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rent_id;
    private String employee;
    private String comments;
    private LocalDate rentDate;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservation_id")
    private ReservationModel reservation;
}
