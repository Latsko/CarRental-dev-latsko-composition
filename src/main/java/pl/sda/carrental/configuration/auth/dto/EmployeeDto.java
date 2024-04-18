package pl.sda.carrental.configuration.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class EmployeeDto extends UserDto {
    @NotEmpty
    private String position;
}
