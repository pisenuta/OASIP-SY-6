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

    private SimpleEventDTO convertEntityToDto(Event event) {
        SimpleEventDTO SimpleEventDTO = new SimpleEventDTO();
        SimpleEventDTO.setId(event.getId());
        SimpleEventDTO.setBookingName(event.getBookingName());
        SimpleEventDTO.setBookingEmail(event.getBookingEmail());
        SimpleEventDTO.setEventStartTime(event.getEventStartTime());
        SimpleEventDTO.setEventNotes(event.getEventNotes());
        return SimpleEventDTO;
    }

    public List<SimpleEventDTO> getAllSimpleEvent() {
        return this.listMapper.mapList(this.repository.findAll(Sort.by("eventStartTime").descending()), SimpleEventDTO.class, this.modelMapper);
    }
}
