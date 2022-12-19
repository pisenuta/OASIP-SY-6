package sit.int221.eventsservice.services;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.HandleExceptionBadRequest;
import sit.int221.eventsservice.dtos.Category.CategoryOwnerDTO;
import sit.int221.eventsservice.dtos.User.LecturerDTO;
import sit.int221.eventsservice.entities.Category;
import sit.int221.eventsservice.entities.CategoryOwner;
import sit.int221.eventsservice.entities.Role;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.repositories.CategoryOwnerRepository;
import sit.int221.eventsservice.repositories.CategoryRepository;
import sit.int221.eventsservice.repositories.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class CategoryOwnerService {
    @Autowired
    private CategoryOwnerRepository categoryOwnerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CategoryOwner configOwner(CategoryOwnerDTO categoryOwnerDTO){
        CategoryOwner owner =  new CategoryOwner();

        User user = this.userRepository.findById(categoryOwnerDTO.getUserId()).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, categoryOwnerDTO.getUserId() + " Does Not Exist");
        });

        Category category = categoryRepository.findById(categoryOwnerDTO.getEventCategoryId()).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, categoryOwnerDTO.getUserId() + " Does Not Exist");
        });

        if(categoryOwnerRepository.findByEventCategoryIdAndUserId(category,user) == null){
            if(user.getRole() == Role.lecturer){
                owner.setEventCategoryId(category);
                owner.setUserId(user);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a lecturer");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already owner");
        }
        owner.setEventCategoryId(category);
        owner.setUserId(user);
        return categoryOwnerRepository.saveAndFlush(modelMapper.map(owner, CategoryOwner.class));
    }

    public ResponseEntity configCateOwner(Integer userId, Integer categoryId){
        User user = this.userRepository.findById(userId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, userId + " Does Not Exist");
        });

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, categoryId + " Does Not Exist");
        });

        user.getEventCategories().stream().map(Category::getId).forEach(id -> {
            if (id.equals(categoryId)) {
                try {
                    throw new HandleExceptionBadRequest("Event Category Id already exists !!!");
                } catch (HandleExceptionBadRequest e) {
                    throw new RuntimeException(e);
                }
            }
        });
        user.getEventCategories().add(category);
        this.userRepository.saveAndFlush(user);
        LecturerDTO lecturerDTO = modelMapper.map(user, LecturerDTO.class);
        return ResponseEntity.status(200).body(lecturerDTO);
    }

    public ResponseEntity deleteCateOwner(Integer userId, Integer categoryId){
        User user = this.userRepository.findById(userId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, userId + " Does Not Exist");
        });

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, categoryId + " Does Not Exist");
        });

        user.getEventCategories().stream().map(Category::getId).forEach(id -> {
            if (id.equals(categoryId)) {
                user.getEventCategories().removeIf(category1 -> category1.getId().equals(categoryId));
                userRepository.saveAndFlush(user);
            }
        });
        this.userRepository.saveAndFlush(user);
        LecturerDTO lecturerDTO = modelMapper.map(user, LecturerDTO.class);
        return ResponseEntity.status(200).body(lecturerDTO);
    }

    public List<CategoryOwner> getAllOwner(){
        return categoryOwnerRepository.findAll();
    }
}
