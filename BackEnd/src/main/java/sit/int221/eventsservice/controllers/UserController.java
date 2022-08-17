package sit.int221.eventsservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.eventsservice.dtos.UserDTO;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.repositories.UserRepository;
import sit.int221.eventsservice.services.UserService;

import javax.validation.Valid;
import java.util.List;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({"/api/users"})
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository repository;

    @GetMapping({""})
    public List<UserDTO> getUsers(){
        return this.userService.getAllUser();
    }

    @GetMapping({"/{Id}"})
    public UserDTO getUserByI(@PathVariable Integer Id){
        return this.userService.getUserById(Id);
    }

    @PostMapping({""})
    public User create(@Valid @RequestBody UserDTO newUser){
        return userService.save(newUser);
    }

}
