package sit.int221.eventsservice.services;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.ApplicationExceptionHandler;
import sit.int221.eventsservice.advice.OverlappedExceptionHandler;
import sit.int221.eventsservice.dtos.SimpleEventDTO;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.entities.Eventcategory;
import sit.int221.eventsservice.repositories.EventRepository;

@Service
public class EventService {
    private final EventRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @Autowired
    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public SimpleEventDTO getSimpleEventById(Integer id) {
        Event event = (Event)this.repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
        });
        return (SimpleEventDTO)this.modelMapper.map(event, SimpleEventDTO.class);
    }

    public List<SimpleEventDTO> getAllSimpleEvent() {
        return this.listMapper.mapList(this.repository.findAll(Sort.by("eventStartTime").descending()), SimpleEventDTO.class, this.modelMapper);
    }

    public Event save(SimpleEventDTO newEvent) throws OverlappedExceptionHandler {
        Date newEventStartTime = Date.from(newEvent.getEventStartTime());
        Date newEventEndTime = findEndDate(Date.from(newEvent.getEventStartTime()), newEvent.getEventDuration());
        List<SimpleEventDTO> eventList = getAllSimpleEvent();
        for (int i = 0; i < eventList.size(); i++) {
            if (newEvent.getEventCategory().getId() == eventList.get(i).getEventCategory().getId()){ //เช็คเฉพาะ EventCategory เดียวกัน
            List errors = new ArrayList();
            Date eventStartTime = Date.from(eventList.get(i).getEventStartTime());
            Date eventEndTime = findEndDate(Date.from(eventList.get(i).getEventStartTime()), eventList.get(i).getEventDuration());
            if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
                    newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
                    newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
                    newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime) ||
                    newEventStartTime.equals(eventStartTime))
            {
                throw new OverlappedExceptionHandler(errors.toString());
            }
        }
        }
        Event e = modelMapper.map(newEvent, Event.class);
        return repository.saveAndFlush(e);
    }

    public Date findEndDate(Date date, Integer duration) {
        return new Date(date.getTime()+(duration*60000+60000));
    }

    public List<SimpleEventDTO> getEventByCategoryId(Eventcategory eventCategoryId){
        List<Event> eventByCategory =repository.findAllByEventCategoryOrderByEventCategoryDesc(eventCategoryId);
        return listMapper.mapList(eventByCategory, SimpleEventDTO.class, modelMapper);
    }

    public List<SimpleEventDTO> getPastEvent(Instant instant) {
        List<Event> pastEvent = repository.findAllByEventStartTimeBeforeOrderByEventStartTimeDesc(instant);
        return listMapper.mapList(pastEvent, SimpleEventDTO.class, modelMapper);
    }

    public List<SimpleEventDTO> getUpcomingEvent(Instant instant) {
        List<Event> pastEvent = repository.findAllByEventStartTimeAfterOrderByEventStartTimeAsc(instant);
        return listMapper.mapList(pastEvent, SimpleEventDTO.class, modelMapper);
    }

    public List<SimpleEventDTO> getEventByDateTime(String startTime, String endTime) {
        List<Event> eventByDateTime = repository.findAllByEventStartTimeBetween(Instant.parse(startTime), Instant.parse(endTime));
        return  listMapper.mapList(eventByDateTime, SimpleEventDTO.class, modelMapper);
    }
//    public Event updateEvent(Integer Id, SimpleEventDTO updateEvent) {
//        Event event = repository.findById(Id).map(e->mapEvent(modelMapper.map(e, SimpleEventDTO.class),updateEvent)).orElseGet(()->{
//            updateEvent.setId(Id);
//            return modelMapper.map(updateEvent, Event.class);
//        });
//        return repository.saveAndFlush(event);
//    }
//
//    public Event mapEvent(SimpleEventDTO event1, SimpleEventDTO event2){
//        event1=event2;
//        return modelMapper.map(event1, Event.class);
//    }
}
