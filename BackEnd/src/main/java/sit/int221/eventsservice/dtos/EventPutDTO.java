package sit.int221.eventsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventPutDTO {
    @NotNull(message = "EventStartTime shouldn't be null.")
    @FutureOrPresent(message = "StartTime mustn't be past.")
    private Instant eventStartTime;
    @Size(max = 500, message = "Note must less or equal then 500.")
    private String eventNotes;

    private CategoryDTO eventCategory;

    private Integer eventDuration;
}
