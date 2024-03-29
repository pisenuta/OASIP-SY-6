package sit.int221.eventsservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public Event() {
    }

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "eventCategoryId", nullable = false)
//    private Category eventCategoryId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "categoryowner",
            joinColumns = {@JoinColumn(name = "userId")},
            inverseJoinColumns = {@JoinColumn(name = "eventCategoryId")})
    private Set<Category> eventCategories = new LinkedHashSet<>();
}