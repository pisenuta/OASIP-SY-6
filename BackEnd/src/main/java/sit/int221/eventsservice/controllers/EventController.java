package sit.int221.eventsservice.controllers;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.dtos.SimpleEventDTO;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.repositories.EventRepository;
import sit.int221.eventsservice.services.EventService;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({"/api/events"})
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventRepository repository;

    public EventController() {
    }

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
        repository.findById(Id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        Id + " does not exist !!!"));
        repository.deleteById(Id);
    }

    @PostMapping({""})
    public Event create(@RequestBody @Valid SimpleEventDTO newEvent) {
        return eventService.save(newEvent);
    }

//    @PutMapping({"/{Id}"})
//    public Event update(@RequestBody @Valid Event updateEvent, @PathVariable Integer Id) {
//        Event event = repository.findById(Id).map(e->mapEvent(e, updateEvent))
//                .orElseGet(()->
//                {
//                    updateEvent.setId(Id);
//                    return updateEvent;
//                });
//        return repository.saveAndFlush(event);
//    }
//
//    private Event mapEvent(Event existingEvent, Event updateEvent) {
//        existingEvent.setId(updateEvent.getId());
//        existingEvent.setBookingName(updateEvent.getBookingName());
//        existingEvent.setBookingEmail(updateEvent.getBookingEmail());
//        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
//        existingEvent.setEventDuration(updateEvent.getEventDuration());
//        existingEvent.setEventNotes(updateEvent.getEventNotes());
//        return existingEvent;
//    }

    @PutMapping({"/{Id}"})
    public SimpleEventDTO updateEvent(@PathVariable Integer Id, @RequestBody SimpleEventDTO updateEvent){
        eventService.updateEvent(Id,updateEvent);
        return updateEvent;
    }


}
