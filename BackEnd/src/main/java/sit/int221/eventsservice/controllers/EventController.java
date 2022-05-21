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
import sit.int221.eventsservice.entities.Eventcategory;
import sit.int221.eventsservice.repositories.EventRepository;
import sit.int221.eventsservice.services.EventService;

import javax.persistence.Id;
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
    public Event create(@Valid @RequestBody SimpleEventDTO newEvent) throws OverlappedExceptionHandler {
//        List<Event> events = repository.findAllByEventCategory(newEvent.getEventCategory().getId());
        return eventService.save(newEvent);
    }

    @PutMapping({"/{Id}"})
    public ResponseEntity update(@Valid @RequestBody EventPutDTO updateEvent, @PathVariable Integer Id) {
        Event event = repository.findById(Id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST)
        );
        modelMapper.map(updateEvent, event);
        return ResponseEntity.status(200).body(event);
    }

    @GetMapping({"/clinic"})
    public List <SimpleEventDTO> getEventByCategory(@RequestParam Eventcategory eventCategoryId) {
        return this.eventService.getEventByCategoryId(eventCategoryId);
    }

    @GetMapping({"/datetime"})
    public List <SimpleEventDTO> getEventByDateTime(@RequestParam String Date) {
        return  this.eventService.getEventByDateTime(Date+"", Date);
    }
}
