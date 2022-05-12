package sit.int221.eventsservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor

@Entity
@Table(
        name = "event"
)
public class Event {
    @Id
    @Column(
            name = "bookingId",
            nullable = false
    )
    private Integer id;
    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "eventCategoryId",
            nullable = false
    )
    private Eventcategory eventCategory;
    @Column(
            name = "bookingName",
            nullable = false,
            length = 100
    )
    private String bookingName;
    @Column(
            name = "bookingEmail",
            nullable = false,
            length = 45
    )
    private String bookingEmail;
    @Column(
            name = "eventStartTime",
            nullable = false
    )
    private Instant eventStartTime;
    @Column(
            name = "eventDuration",
            nullable = false
    )
    private Integer eventDuration;
    @Lob
    @Column(
            name = "eventNotes"
    )
    private String eventNotes;

    public Event() {
    }
}