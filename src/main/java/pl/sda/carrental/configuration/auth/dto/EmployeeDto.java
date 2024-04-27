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

    public EmployeeDto(Long id,
                       String login,
                       String password,
                       String fullName,
                       Long branch,
                       String position) {
        super(id, login, password, fullName, branch);
        this.position = position;
    }
}
