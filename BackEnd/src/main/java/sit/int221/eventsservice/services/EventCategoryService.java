package sit.int221.eventsservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.dtos.SimpleEventCategoryDTO;
import sit.int221.eventsservice.dtos.SimpleEventDTO;
import sit.int221.eventsservice.entities.Eventcategory;
import sit.int221.eventsservice.repositories.EventcategoryRepository;

import java.util.List;

@Service
public class EventCategoryService {
    private final EventcategoryRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @Autowired
    public EventCategoryService(EventcategoryRepository repository) {
        this.repository = repository;
    }

    public SimpleEventCategoryDTO getSimpleEventCategoryById(Integer id) {
        Eventcategory eventcategory = (Eventcategory)this.repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
        });
        return (SimpleEventCategoryDTO) this.modelMapper.map(eventcategory, SimpleEventCategoryDTO.class);
    }

//    private SimpleEventCategoryDTO convertEntityToDto(Eventcategory eventcategory){
//        SimpleEventCategoryDTO SimpleEventCategoryDTO = new SimpleEventCategoryDTO();
//        SimpleEventDTO.setId(eventcategory.getId());
//        SimpleEventCategoryDTO.setEventCategoryName(eventcategory.getEventCategoryName());
//        SimpleEventCategoryDTO.setEventCategoryDescription(eventcategory.getEventCategoryDescription());
//        SimpleEventCategoryDTO.setEventDuration(eventcategory.getEventDuration());
//        return SimpleEventCategoryDTO;
//    }

    public List<SimpleEventCategoryDTO> getAllSimpleEventCategory() {
        List<Eventcategory> eventcategories = repository.findAll();
        return listMapper.mapList(eventcategories, SimpleEventCategoryDTO.class, modelMapper);
    }
}