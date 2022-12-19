package sit.int221.eventsservice.dtos.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryOwnerDTO {
    private Integer userId;
    private Integer eventCategoryId;
}
