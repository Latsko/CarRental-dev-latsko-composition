package pl.sda.carrental.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "revenue")
public class Revenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "revenue_id")
    private Long revenueId;

    private BigDecimal totalAmount;
    // todo: Brak relacji z innymi encjami. Jeśli dochód jest przypisany do konkretnego oddziału (Branch), rozważ dodanie relacji do tej encji.

    @OneToOne(mappedBy = "revenue")
    private Branch branch;
}
