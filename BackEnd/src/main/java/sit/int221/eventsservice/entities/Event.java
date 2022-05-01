package sit.int221.eventsservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.Instant;

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

    public String getEventNotes() {
        return this.eventNotes;
    }

    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public Integer getEventDuration() {
        return this.eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public Instant getEventStartTime() {
        return this.eventStartTime;
    }

    public void setEventStartTime(Instant eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getBookingEmail() {
        return this.bookingEmail;
    }

    public void setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail;
    }

    public String getBookingName() {
        return this.bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public Eventcategory getEventCategory() {
        return this.eventCategory;
    }

    public void setEventCategory(Eventcategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}