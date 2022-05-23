package sit.int221.eventsservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.dtos.SimpleCategoryDTO;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public SimpleCategoryDTO getSimpleEventCategoryById(Integer id) {
        Category category = (Category)this.repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
        });
        return (SimpleCategoryDTO) this.modelMapper.map(category, SimpleCategoryDTO.class);
    }

    public List<SimpleCategoryDTO> getAllSimpleEventCategory() {
        List<Category> eventcategories = repository.findAll();
        return listMapper.mapList(eventcategories, SimpleCategoryDTO.class, modelMapper);
    }
}
