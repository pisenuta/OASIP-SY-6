package sit.int221.eventsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleEventCategoryDTO {
    private Integer id;
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;
}
