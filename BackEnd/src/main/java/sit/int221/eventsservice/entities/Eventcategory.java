package sit.int221.eventsservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(
        name = "eventcategory"
)
public class Eventcategory {
    @Id
    @Column(
            name = "eventCategoryId",
            nullable = false
    )
    private Integer id;
    @Column(
            name = "eventCategoryName",
            nullable = false,
            length = 100
    )
    private String eventCategoryName;
    @Lob
    @Column(
            name = "eventCategoryDescription"
    )
    private String eventCategoryDescription;
    @Column(
            name = "eventDuration",
            nullable = false
    )
    private Integer eventDuration;
    @JsonIgnore
    @OneToMany(
            mappedBy = "eventCategory"
    )
    private Set<Event> events = new LinkedHashSet();

    public Eventcategory() {
    }
}