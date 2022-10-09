package sit.int221.eventsservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.dtos.Category.CategoryDTO;
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

    public CategoryDTO getSimpleEventCategoryById(Integer id) {
        Category category = (Category)this.repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
        });
        return (CategoryDTO) this.modelMapper.map(category, CategoryDTO.class);
    }

    public List<CategoryDTO> getAllEventCategory() {
        List<Category> eventcategories = repository.findAll();
        return listMapper.mapList(eventcategories, CategoryDTO.class, modelMapper);
    }
}
