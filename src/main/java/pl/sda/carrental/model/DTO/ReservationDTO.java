package pl.sda.carrental.model.DTO;

import jakarta.validation.constraints.NotNull;
import pl.sda.carrental.model.Rent;
import pl.sda.carrental.model.Returnal;

import java.time.LocalDate;

public record ReservationDTO(
        Long reservation_id,
        @NotNull Long customer_id,
        @NotNull Long car_id,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull Long startBranchId,
        @NotNull Long endBranchId,
        Rent rent,
        Returnal returnal
        ) {
}
