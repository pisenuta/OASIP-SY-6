package sit.int221.eventsservice.dtos;

import lombok.*;
import sit.int221.eventsservice.entities.Role;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private Role role;
    private Instant createdOn;
    private Instant updatedOn;
}
