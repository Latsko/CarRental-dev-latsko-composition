package pl.sda.carrental.configuration.security.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class UserDto {
    private Long id;
    @NotEmpty(message = "Name should not be empty")
    private String login;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    private String position;
    private String email;
}
