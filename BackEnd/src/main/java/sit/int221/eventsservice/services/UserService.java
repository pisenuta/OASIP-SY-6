package sit.int221.eventsservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.ApplicationExceptionHandler;
import sit.int221.eventsservice.advice.CheckUniqueUserExceptionHandler;
import sit.int221.eventsservice.dtos.User.UserCreateDTO;
import sit.int221.eventsservice.dtos.User.UserDTO;
import sit.int221.eventsservice.dtos.User.UserLoginDTO;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.repositories.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;
    private ServletWebRequest ServletWebRequest;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<UserDTO> getAllUser() {
        return this.listMapper.mapList(this.repository.findAll(Sort.by("name").ascending()), UserDTO.class, this.modelMapper);
    }

    public UserDTO getUserById(Integer id) {
        User user = this.repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
        });
        return this.modelMapper.map(user, UserDTO.class);
    }

    public User save(UserDTO newUser) throws CheckUniqueUserExceptionHandler {
        List<UserDTO> userList = getAllUser();
        for(int i = 0; i < userList.size(); i++){
            if (newUser.getName().trim().equals(userList.get(i).getName())){
                throw new CheckUniqueUserExceptionHandler("User name must be unique.");
            } else if (newUser.getEmail().trim().equals(userList.get(i).getEmail())){
                throw new CheckUniqueUserExceptionHandler("User email must be unique.");
            }
        }
        newUser.setName(newUser.getName().trim());
        newUser.setEmail(newUser.getEmail().trim());
        newUser.setPassword(argon2PasswordEncoder.encode(newUser.getPassword()));
        User user = modelMapper.map(newUser, User.class);
        return repository.saveAndFlush(user);
    }

    public UserLoginDTO Login (UserLoginDTO user){
        if(repository.existsByEmail(user.getEmail())){
            User userdb = repository.findByEmail(user.getEmail());
            if(argon2PasswordEncoder.matches(user.getPassword(), userdb.getPassword())){
                throw new ResponseStatusException(HttpStatus.OK, "successes");
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not success");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user dose not exist");
        }
    }
}
