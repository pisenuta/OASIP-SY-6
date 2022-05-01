package sit.int221.eventsservice.dtos;

public class SimpleEventCategoryDTO {
    private String eventCategoryName;
    private int eventDuration;

    public String getEventCategoryName() {
        return this.eventCategoryName;
    }

    public int getEventDuration() {
        return this.eventDuration;
    }

    public void setEventCategoryName(final String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }

    public void setEventDuration(final int eventDuration) {
        this.eventDuration = eventDuration;
    }

    public SimpleEventCategoryDTO() {
    }

    public SimpleEventCategoryDTO(final String eventCategoryName, final int eventDuration) {
        this.eventCategoryName = eventCategoryName;
        this.eventDuration = eventDuration;
    }
}
