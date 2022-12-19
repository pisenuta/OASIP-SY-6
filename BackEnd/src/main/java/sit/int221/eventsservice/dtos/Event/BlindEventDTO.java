package sit.int221.eventsservice.dtos.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.eventsservice.entities.Category;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlindEventDTO {
    private Integer Id;
    private Category categoryId;
    private Instant eventStartTime;
    private Integer eventDuration;
}
