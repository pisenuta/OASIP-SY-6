package sit.int221.eventsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEventDTO {
    private Integer id;
    @NotNull(message = "EventCategory shouldn't be null.")
    private SimpleEventCategoryDTO eventCategory;
    @NotBlank(message = "Name shouldn't be null or empty.")
    @Size(max = 100 , message = "Name must less or equal then 100.")
    private String bookingName;
    @Email(message = "Invalid email address.")
    @Size(min = 1, max = 45)
    private String bookingEmail;
    @NotNull(message = "EventStartTime shouldn't be null.")
    private Instant eventStartTime;
    @NotNull(message = "Duration shouldn't be null.")
    private Integer eventDuration;
    @Size(max = 500, message = "Note must less or equal then 500.")
    private String eventNotes;

}
