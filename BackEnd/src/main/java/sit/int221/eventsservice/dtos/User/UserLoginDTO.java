package sit.int221.eventsservice.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.eventsservice.entities.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @Size(max = 45, message = "Email must less or equal then 45.")
    @Email(message = "Invalid email address.")
    private String email;
    @NotBlank(message = "Password shouldn't be null or empty.")
    @Size(min = 8, max = 14, message = "Password must be between 8 and 14.")
    private String password;
}
