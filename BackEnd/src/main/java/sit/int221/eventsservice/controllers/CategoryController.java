package sit.int221.eventsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.eventsservice.dtos.Category.CategoryDTO;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.repositories.CategoryRepository;
import sit.int221.eventsservice.services.CategoryService;

import javax.validation.Valid;
import java.util.List;
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({"/api/categories"})
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping({""})
    public List<CategoryDTO> getCategory() {
        return this.categoryService.getAllEventCategory();
    }

    @PutMapping({"/{Id}"})
    public Category updateCategory(@Valid @RequestBody Category updateCategory, @PathVariable Integer Id) {
        Category category = categoryRepository.findById(Id).map(c->mapCategory(c, updateCategory))
                .orElseGet(()->
                {   
                    updateCategory.setId(Id);
                    return updateCategory;
                });
        return categoryRepository.saveAndFlush(category);
    }

    private Category mapCategory(Category existingEventcat, Category updateCategory) {
        existingEventcat.setEventCategoryName(updateCategory.getEventCategoryName().trim());
        existingEventcat.setEventCategoryDescription(updateCategory.getEventCategoryDescription());
        existingEventcat.setEventDuration(updateCategory.getEventDuration());
        return existingEventcat;
    }
}