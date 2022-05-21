package sit.int221.eventsservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.constraints.*;

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
    @NotBlank(message = "Clinic name shouldn't be null or empty.")
    @Size(max = 100 , message = "Name must less or equal then 100.")
    @UniqueElements(message = "Clinic name must be unique.")
    private String eventCategoryName;
    @Lob
    @Column(
            name = "eventCategoryDescription"
    )
    @Size(max = 500, message = "Note must less or equal then 500.")
    private String eventCategoryDescription;
    @Column(
            name = "eventDuration",
            nullable = false
    )
    @NotNull(message = "Duration shouldn't be null")
    @Max(value = 480, message = "Duration must less or equal then 480.")
    @Min(value = 1 , message = "Duration must more then 1.")
    private Integer eventDuration;
    @JsonIgnore
    @OneToMany(
            mappedBy = "eventCategory"
    )
    private Set<Event> events = new LinkedHashSet();

    public Eventcategory() {
    }
}