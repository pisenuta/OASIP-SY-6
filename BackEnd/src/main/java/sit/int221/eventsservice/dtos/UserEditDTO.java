package sit.int221.eventsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDTO {
    @NotNull(message = "Name shouldn't be null.")
    private String name;
    @NotNull(message = "Email shouldn't be null.")
    private String email;
    private String role;
}
