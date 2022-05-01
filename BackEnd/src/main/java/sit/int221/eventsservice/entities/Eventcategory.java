package sit.int221.eventsservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

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

    public Set<Event> getEvents() {
        return this.events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Integer getEventDuration() {
        return this.eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getEventCategoryDescription() {
        return this.eventCategoryDescription;
    }

    public void setEventCategoryDescription(String eventCategoryDescription) {
        this.eventCategoryDescription = eventCategoryDescription;
    }

    public String getEventCategoryName() {
        return this.eventCategoryName;
    }

    public void setEventCategoryName(String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}