package sit.int221.eventsservice.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.OverlappedExceptionHandler;
import sit.int221.eventsservice.dtos.EventPutDTO;
import sit.int221.eventsservice.dtos.SimpleEventDTO;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.repositories.EventRepository;
import sit.int221.eventsservice.services.EventService;

import javax.validation.Valid;

@RestController
@RequestMapping({"/api/events"})
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventRepository repository;

    @GetMapping({"/{Id}"})
    public SimpleEventDTO getEventById(@PathVariable Integer Id) {
        return this.eventService.getSimpleEventById(Id);
    }

    @GetMapping({""})
    public List<SimpleEventDTO> getEvents() {
        return this.eventService.getAllSimpleEvent();
    }

    @DeleteMapping({"/{Id}"})
    public void delete(@PathVariable Integer Id) {
        repository.findById(Id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        Id + " does not exist !!!"));
        repository.deleteById(Id);
    }

    @PostMapping({""})
    public Event create(@Valid @RequestBody SimpleEventDTO newEvent) throws OverlappedExceptionHandler {
        return eventService.save(newEvent);
    }

    @PutMapping({"/{Id}"})
    public ResponseEntity update(@Valid @RequestBody EventPutDTO updateEvent, @PathVariable Integer Id) throws OverlappedExceptionHandler {
        Date newEventStartTime = Date.from(updateEvent.getEventStartTime());
        Date newEventEndTime = eventService.findEndDate(Date.from(updateEvent.getEventStartTime()), updateEvent.getEventDuration());
        List<SimpleEventDTO> eventList = getEvents();
        for (int i = 0; i < eventList.size(); i++) {
            if (updateEvent.getEventCategory().getId() == eventList.get(i).getEventCategory().getId()&& eventList.get(i).getId() != Id) { //เช็คเฉพาะ EventCategory เดียวกัน และถ้าอัพเดตตัวเดิมไม่ต้องเช็ค overlapped
                List errors = new ArrayList();
                Date eventStartTime = Date.from(eventList.get(i).getEventStartTime());
                Date eventEndTime = eventService.findEndDate(Date.from(eventList.get(i).getEventStartTime()), eventList.get(i).getEventDuration());
                if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
                        newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
                        newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
                        newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime) ||
                        newEventStartTime.equals(eventStartTime)) {
                    throw new OverlappedExceptionHandler(errors.toString());
                }
            }
        }
        Event event = repository.findById(Id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        modelMapper.map(updateEvent, event);
        repository.saveAndFlush(event);
        return ResponseEntity.status(200).body(event);
    }


    @GetMapping({"/clinic"})
    public List <SimpleEventDTO> getEventByCategory(@RequestParam Category eventCategoryId) {
        return this.eventService.getEventByCategoryId(eventCategoryId);
    }

    @GetMapping({"/datetime"})
    public List <SimpleEventDTO> getEventByDateTime(@RequestParam String Date) {
        return  this.eventService.getEventByDateTime(Date+"T00:00:00Z", Date+"T23:59:00Z");
    }

    @GetMapping({"/schedule-past"})
    public List <SimpleEventDTO> getPastEvent(@RequestParam Instant DateTime) {
        return this.eventService.getPastEvent(DateTime);
    }

    @GetMapping({"/schedule-comingup"})
    public List <SimpleEventDTO> getUpcomingEvent(@RequestParam Instant DateTime) {
        return this.eventService.getUpcomingEvent(DateTime);
    }
}
