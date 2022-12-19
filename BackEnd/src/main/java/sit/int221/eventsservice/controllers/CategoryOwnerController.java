package sit.int221.eventsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.eventsservice.entities.CategoryOwner;
import sit.int221.eventsservice.services.CategoryOwnerService;

import java.util.List;

@RestController
@RequestMapping("/api/config-owner")
public class CategoryOwnerController {
    @Autowired
    private CategoryOwnerService categoryOwnerService;

    @PostMapping("/{userId}/{categoryId}")
    public ResponseEntity configOwner(@PathVariable Integer userId, @PathVariable Integer categoryId){
        return categoryOwnerService.configCateOwner(userId, categoryId);
    }

    @DeleteMapping("/{userId}/{categoryId}")
    public ResponseEntity deleteOwner(@PathVariable Integer userId, @PathVariable Integer categoryId){
        return categoryOwnerService.deleteCateOwner(userId, categoryId);
    }

//    @GetMapping("")
//    public List<CategoryOwner> getAllOwner(){
//        return categoryOwnerService.getAllOwner();
//    }
}
