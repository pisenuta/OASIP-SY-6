package sit.int221.eventsservice.controllers;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.HandleExceptionBadRequest;
import sit.int221.eventsservice.advice.HandleExceptionForbidden;
import sit.int221.eventsservice.advice.OverlappedExceptionHandler;
import sit.int221.eventsservice.dtos.Event.EventPostDTO;
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
    private EventRepository repository;

    @Autowired
    private UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

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

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Event create(@Valid @RequestPart("event") String newEvent, @RequestPart(value = "file", required = false) MultipartFile file)
            throws OverlappedExceptionHandler, HandleExceptionBadRequest, JsonProcessingException, HandleExceptionForbidden {
        objectMapper.registerModule(new JavaTimeModule());
        EventPostDTO eventPost = objectMapper.readValue(newEvent, EventPostDTO.class);
        return eventService.save(eventPost, file);
    }

    @PostMapping({"/guest"})
    public Event guestCreate(@Valid @RequestPart("event") String newEvent, @RequestPart(value = "file", required = false) MultipartFile file)
            throws OverlappedExceptionHandler, HandleExceptionBadRequest, JsonProcessingException, HandleExceptionForbidden {
        objectMapper.registerModule(new JavaTimeModule());
        EventPostDTO eventPost = objectMapper.readValue(newEvent, EventPostDTO.class);
        return eventService.save(eventPost, file);
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
