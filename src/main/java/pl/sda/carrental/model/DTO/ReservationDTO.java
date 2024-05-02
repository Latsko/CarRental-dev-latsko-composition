package pl.sda.carrental.model.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.With;

import java.time.LocalDate;

@With
public record ReservationDTO(
        Long reservation_id,
        @NotNull Long customer_id,
        @NotNull Long car_id,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull Long startBranchId,
        @NotNull Long endBranchId
        ) {
}
