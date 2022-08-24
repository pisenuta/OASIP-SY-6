package sit.int221.eventsservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Category eventCategory;
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
    @JsonFormat(timezone="Asia/Bangkok")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
    
    public Event() {
    }
}