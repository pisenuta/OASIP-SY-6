package sit.int221.eventsservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.eventsservice.dtos.UserDTO;
import sit.int221.eventsservice.repositories.UserRepository;
import sit.int221.eventsservice.services.UserService;

import java.util.List;

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
}
