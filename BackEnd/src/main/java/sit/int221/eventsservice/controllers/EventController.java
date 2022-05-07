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
    public Event create(@RequestBody SimpleEventDTO newEvent) {
        return eventService.save(newEvent);
    }
//    @PostMapping({""})
//    public Event create(@RequestBody Event newEvent) {
//        return eventService.save(newEvent);
//    }

}
