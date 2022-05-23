package sit.int221.eventsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEventDTO {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "EventCategory shouldn't be null.")
    private SimpleCategoryDTO eventCategory;
    @NotBlank(message = "Name shouldn't be null or empty.")
    @Size(max = 100 , message = "Name must less or equal then 100.")
    private String bookingName;
    @Email(message = "Invalid email address.")
    @NotBlank(message = "Email shouldn't be null or empty.")
    @Size(max = 45, message = "Email must less or equal then 45.")
    private String bookingEmail;
    @NotNull(message = "EventStartTime shouldn't be null.")
    @FutureOrPresent(message = "StartTime mustn't be past.")
    private Instant eventStartTime;
    @NotNull(message = "Duration shouldn't be null.")
    private Integer eventDuration;
    @Size(max = 500, message = "Note must less or equal then 500.")
    private String eventNotes;
}
