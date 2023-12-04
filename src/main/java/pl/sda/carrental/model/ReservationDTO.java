package pl.sda.carrental.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationDTO(
        @NotNull Long customer_id,
        @NotNull Long car_id,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull Long startBranchId,
        @NotNull Long endBranchId
        ) {
}
