package sit.int221.eventsservice.controllers;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.eventsservice.dtos.SimpleEventDTO;
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
}
