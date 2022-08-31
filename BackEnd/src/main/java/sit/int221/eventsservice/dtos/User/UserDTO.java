package sit.int221.eventsservice.dtos.User;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;
import sit.int221.eventsservice.entities.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userId;
    @NotBlank(message = "Name shouldn't be null or empty.")
    @Size(max = 100 , message = "Name must less or equal then 100.")
    private String name;
    @NotBlank(message = "Email shouldn't be null or empty.")
    @Size(max = 45, message = "Email must less or equal then 45.")
    @Email(message = "Invalid email address.")
    private String email;
    @NotBlank(message = "Password shouldn't be null or empty.")
    @Size(min = 8, max = 14, message = "Password must be between 8 and 14.")
    private String password;
    private Role role;
    private Instant createdOn;
    private Instant updatedOn;
}