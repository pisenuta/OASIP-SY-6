package sit.int221.eventsservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.CheckUniqueUserExceptionHandler;
import sit.int221.eventsservice.dtos.User.UserCreateDTO;
import sit.int221.eventsservice.dtos.User.UserDTO;
import sit.int221.eventsservice.dtos.User.UserEditDTO;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.repositories.UserRepository;
import sit.int221.eventsservice.services.UserService;

import javax.validation.Valid;
import java.util.List;
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({"/api/users"})
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    @GetMapping({""})
    public List<UserDTO> getUsers() {
        return this.userService.getAllUser();
    }

    @GetMapping({"/{Id}"})
    public UserDTO getUserById(@PathVariable Integer Id , WebRequest request) {
        return this.userService.getUserById(Id);
    }

    @PostMapping({"/register"})
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody UserCreateDTO newUser) throws CheckUniqueUserExceptionHandler {
        return userService.save(newUser);
    }

    @DeleteMapping({"/{Id}"})
    public void delete(@PathVariable Integer Id) {
        repository.findById(Id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        Id + " does not exist !!!"));
        repository.deleteById(Id);
    }

    @PutMapping({"/{Id}"})
    public ResponseEntity update(@Valid @RequestBody UserEditDTO updateUser, @PathVariable Integer Id) throws CheckUniqueUserExceptionHandler {
        List<UserDTO> userList = getUsers();

        updateUser.setName(updateUser.getName().trim());
        updateUser.setEmail(updateUser.getEmail().trim());

        for(int i = 0; i < userList.size(); i++) {
            if(updateUser.getName().trim().equals(userList.get(i).getName()) && updateUser.getEmail().trim().equals(userList.get(i).getEmail())
                    && userList.get(i).getUserId() != Id && userList.get(i).getUserId() != Id) {
                throw new CheckUniqueUserExceptionHandler("User and Email already exist");
            }else if(updateUser.getName().trim().equals(userList.get(i).getName()) && userList.get(i).getUserId() != Id){
                throw new CheckUniqueUserExceptionHandler("User name must be unique.");
            } else if(updateUser.getEmail().trim().equals(userList.get(i).getEmail()) && userList.get(i).getUserId() != Id){
                throw new CheckUniqueUserExceptionHandler("User email must be unique.");
            }
        }
            User user = repository.findById(Id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,
                            Id + " does not exist !!!"));

        modelMapper.map(updateUser, user);
        repository.saveAndFlush(user);
        return ResponseEntity.status(200).body(user);
    }
}
