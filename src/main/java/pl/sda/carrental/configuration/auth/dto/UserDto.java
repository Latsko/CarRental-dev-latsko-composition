package pl.sda.carrental.configuration.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private String fullName;
    private Long branchId;
}
