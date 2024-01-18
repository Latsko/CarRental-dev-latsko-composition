package pl.sda.carrental.model.DTO;

import java.math.BigDecimal;

public record CarDTO(String make,
                     String model,
                     String bodyStyle,
                     int year,
                     String color,
                     double mileage,
                     BigDecimal price){
}
