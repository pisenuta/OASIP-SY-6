package sit.int221.eventsservice.dtos;

import lombok.*;
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
    private Role role;
    private Instant createdOn;
    private Instant updatedOn;
}
