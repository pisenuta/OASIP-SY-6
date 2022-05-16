package sit.int221.eventsservice.services;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.dtos.SimpleEventDTO;
import sit.int221.eventsservice.entities.Event;
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

    public Event save(SimpleEventDTO newEvent) {
        Event e = modelMapper.map(newEvent, Event.class);
        return repository.saveAndFlush(e);
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
