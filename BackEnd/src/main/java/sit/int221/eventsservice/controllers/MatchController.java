package sit.int221.eventsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.eventsservice.dtos.User.UserLoginDTO;
import sit.int221.eventsservice.services.UserService;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({"/api/match"})
public class MatchController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public Object Login(@Valid @RequestBody UserLoginDTO user){
        return userService.Login(user);
    }
}
