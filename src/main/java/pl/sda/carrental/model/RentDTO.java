package pl.sda.carrental.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RentDTO(@NotNull String employee,
                      String comments,
                      @NotNull LocalDate rentDate,
                      @NotNull Long reservationId) {
}
