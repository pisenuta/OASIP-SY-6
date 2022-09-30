package sit.int221.eventsservice.dtos.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.eventsservice.dtos.Category.CategoryDTO;

import javax.validation.constraints.*;
import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventPutDTO {
    @Email(message = "Invalid email address.")
    @NotBlank(message = "Email shouldn't be null or empty.")
    @Size(max = 45, message = "Email must less or equal then 45.")
    private String bookingEmail;
    @NotNull(message = "EventStartTime shouldn't be null.")
    @FutureOrPresent(message = "StartTime mustn't be past.")
    private Instant eventStartTime;
    @Size(max = 500, message = "Note must less or equal then 500.")
    private String eventNotes;

    private CategoryDTO eventCategory;

    private Integer eventDuration;
}
