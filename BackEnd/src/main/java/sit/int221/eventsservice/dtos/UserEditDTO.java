package sit.int221.eventsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.eventsservice.entities.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDTO {
    @NotNull(message = "Name shouldn't be null.")
    private String name;
    @NotNull(message = "Email shouldn't be null.")
    @NotBlank(message = "Email shouldn't be null or empty.")
    @Size(max = 45, message = "Email must less or equal then 45.")
    @Email(message = "Invalid email address.")
    private String email;
    private Role role;
}
