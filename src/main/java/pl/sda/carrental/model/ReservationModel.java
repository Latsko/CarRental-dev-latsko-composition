package pl.sda.carrental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reservation")
public class ReservationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservation_id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private ClientModel customer;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private CarModel car;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @DecimalMin(value = "1.00", message = "Price must be grater than 1.00")
    @DecimalMax(value = "100000.00", message = "Price must be lesser than 100000.00")
    @Digits(integer = 7, fraction = 2, message = "Price must have up to 7 digits in total and 2 decimal places")
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "start_branch_id")
    private BranchModel startBranch;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "end_branch_id")
    private BranchModel endBranch;
}
