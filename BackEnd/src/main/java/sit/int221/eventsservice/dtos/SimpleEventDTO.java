package sit.int221.eventsservice.dtos;

import java.time.Instant;

public class SimpleEventDTO {
    private Integer id;
    private SimpleEventCategoryDTO eventCategory;
    private String bookingName;
    private String bookingEmail;
    private Instant eventStartTime;
    private String eventNotes;

    public Integer getId() {
        return this.id;
    }

    public SimpleEventCategoryDTO getEventCategory() {
        return this.eventCategory;
    }

    public String getBookingName() {
        return this.bookingName;
    }

    public String getBookingEmail() {
        return this.bookingEmail;
    }

    public Instant getEventStartTime() {
        return this.eventStartTime;
    }

    public String getEventNotes() {
        return this.eventNotes;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setEventCategory(final SimpleEventCategoryDTO eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void setBookingName(final String bookingName) {
        this.bookingName = bookingName;
    }

    public void setBookingEmail(final String bookingEmail) {
        this.bookingEmail = bookingEmail;
    }

    public void setEventStartTime(final Instant eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public void setEventNotes(final String eventNotes) {
        this.eventNotes = eventNotes;
    }

    public SimpleEventDTO() {
    }

    public SimpleEventDTO(final Integer id, final SimpleEventCategoryDTO eventCategory, final String bookingName, final String bookingEmail, final Instant eventStartTime, final String eventNotes) {
        this.id = id;
        this.eventCategory = eventCategory;
        this.bookingName = bookingName;
        this.bookingEmail = bookingEmail;
        this.eventStartTime = eventStartTime;
        this.eventNotes = eventNotes;
    }
}
