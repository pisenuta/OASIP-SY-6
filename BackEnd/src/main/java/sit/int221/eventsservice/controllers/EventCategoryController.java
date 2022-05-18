package sit.int221.eventsservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.eventsservice.dtos.SimpleEventCategoryDTO;
import sit.int221.eventsservice.dtos.SimpleEventDTO;
import sit.int221.eventsservice.entities.Event;
import sit.int221.eventsservice.entities.Eventcategory;
import sit.int221.eventsservice.repositories.EventRepository;
import sit.int221.eventsservice.repositories.EventcategoryRepository;
import sit.int221.eventsservice.services.EventCategoryService;
import sit.int221.eventsservice.services.EventService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({"/api/eventcategory"})
public class EventCategoryController {
    @Autowired
    private EventCategoryService eventCategoryService;

    @Autowired
    private ModelMapper modelMapper;
    private EventcategoryRepository repository;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventcategoryRepository eventcategoryRepository;

    public EventCategoryController() {
    }

    @GetMapping({"/{Id}"})
    public SimpleEventCategoryDTO getSimpleEventCategoryById(@PathVariable Integer Id) {
        return this.eventCategoryService.getSimpleEventCategoryById(Id);
    }

    @GetMapping({""})
    public List<SimpleEventCategoryDTO> getEventcategory() {
        return this.eventCategoryService.getAllSimpleEventCategory();
    }

    @PutMapping({"/{Id}"})
    public Eventcategory updateCategory(@RequestBody Eventcategory updateEventcategory, @PathVariable Integer Id) {
        Eventcategory eventcategory = eventcategoryRepository.findById(Id).map(c->mapEventcategory(c, updateEventcategory))
                .orElseGet(()->
                {
                    updateEventcategory.setId(Id);
                    return updateEventcategory;
                });
        return eventcategoryRepository.saveAndFlush(eventcategory);
    }

    private Eventcategory mapEventcategory(Eventcategory existingEventcat, Eventcategory updateEventcategory) {
        existingEventcat.setEventCategoryName(updateEventcategory.getEventCategoryName());
        existingEventcat.setEventCategoryDescription(updateEventcategory.getEventCategoryDescription());
        existingEventcat.setEventDuration(updateEventcategory.getEventDuration());
        return existingEventcat;
    }
}