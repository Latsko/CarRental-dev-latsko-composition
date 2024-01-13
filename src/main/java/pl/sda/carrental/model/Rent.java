package pl.sda.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rent_id;// fixMe: tu powinien być 'camelCase' a nad nazwą pola powinna być nadana nazwa jako 'snakeCase'
    private String comments;
    private LocalDate rentDate;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "reservation_id")
//    @JsonBackReference(value = "reservationRent-reference") // wydaje się nadmiarowe
    private Reservation reservation;
}
