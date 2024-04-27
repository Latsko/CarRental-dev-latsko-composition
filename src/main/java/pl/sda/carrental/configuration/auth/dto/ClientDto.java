package pl.sda.carrental.configuration.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@With
public class ClientDto extends UserDto {
    @NotEmpty
    private String email;
    private String address;

    public ClientDto(Long id,
                     String login,
                     String password,
                     String fullName,
                     Long branch,
                     String email, String address) {
        super(id, login, password, fullName, branch);
        this.email = email;
        this.address = address;
    }
}
