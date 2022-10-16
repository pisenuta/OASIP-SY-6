package sit.int221.eventsservice.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.HandleExceptionBadRequest;
import sit.int221.eventsservice.advice.HandleExceptionForbidden;
import sit.int221.eventsservice.advice.OverlappedExceptionHandler;
import sit.int221.eventsservice.dtos.Event.EventPutDTO;
import sit.int221.eventsservice.dtos.Event.EventDTO;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.entities.Role;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.repositories.EventRepository;
import sit.int221.eventsservice.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/{Id}"})
    public EventDTO getEventById(@PathVariable Integer Id) throws HandleExceptionForbidden {
        return this.eventService.getEventById(Id);
    }

    @GetMapping({""})
    public List<EventDTO> getEvents() {
        return this.eventService.getAllEvent();
    }

    @DeleteMapping({"/{Id}"})
    public void delete(@PathVariable Integer Id) throws HandleExceptionForbidden {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userLogin = userRepository.findByEmail(auth.getPrincipal().toString());
        if (userLogin.getRole().equals(Role.admin)) {
            repository.findById(Id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            Id + " does not exist !!!"));
            repository.deleteById(Id);
        } else if (userLogin.getRole().equals(Role.student)) {
            Event event = repository.findById(Id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            Id + " does not exist !!!"));
            if (Objects.equals(event.getUser().getEmail(), userLogin.getEmail())) {
                repository.deleteById(Id);
            } else {
                throw new HandleExceptionForbidden("You are not owner of this event");
            }
        } else {
            throw new HandleExceptionForbidden("You are not allowed to delete event");
        }
    }

    @PostMapping({""})
    public Event create(@Valid @RequestBody EventDTO newEvent) throws OverlappedExceptionHandler, HandleExceptionForbidden, HandleExceptionBadRequest {
        return eventService.save(newEvent);
    }

    @PostMapping({"/guest"})
    public Event guestCreate(@Valid @RequestBody EventDTO newEvent) throws OverlappedExceptionHandler, HandleExceptionForbidden, HandleExceptionBadRequest {
        return eventService.save(newEvent);
    }

    @PutMapping({"/{Id}"})
    public ResponseEntity<Event> update(@Valid @RequestBody EventPutDTO updateEvent, @PathVariable Integer Id) throws OverlappedExceptionHandler, HandleExceptionForbidden {
        return eventService.update(updateEvent, Id);
    }

    @GetMapping({"/clinic"})
    public List <EventDTO> getEventByCategory(@RequestParam Category eventCategoryId) throws HandleExceptionForbidden {
        return this.eventService.getEventByCategoryId(eventCategoryId);
    }

    @GetMapping({"/datetime"})
    public List <EventDTO> getEventByDateTime(@RequestParam String Date) throws HandleExceptionForbidden {
        return  this.eventService.getEventByDateTime(Date+"T00:00:00Z", Date+"T23:59:00Z");
    }

    @GetMapping({"/schedule-past"})
    public List <EventDTO> getPastEvent(@RequestParam Instant DateTime) {
        return this.eventService.getPastEvent(DateTime);
    }

    @GetMapping({"/schedule-comingup"})
    public List <EventDTO> getUpcomingEvent(@RequestParam Instant DateTime) {
        return this.eventService.getUpcomingEvent(DateTime);
    }
}
