package sit.int221.eventsservice.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.entities.Role;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDTO {
    private Integer userId;
    private String name;
    private String email;
    private Role role;
    private Instant createdOn;
    private Instant updatedOn;
    private Set<Category> categories;
}
