package sit.int221.eventsservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.*;
import sit.int221.eventsservice.dtos.User.UserCreateDTO;
import sit.int221.eventsservice.dtos.User.UserDTO;
import sit.int221.eventsservice.dtos.User.UserLoginDTO;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.repositories.UserRepository;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if(newUser.getName().trim().equals(userList.get(i).getName()) && userList.get(i).getUserId() != newUser.getUserId()
                    && newUser.getEmail().trim().equals(userList.get(i).getEmail()) && userList.get(i).getUserId() != newUser.getUserId()){
                throw new CheckUniqueUserExceptionHandler("User and Email already exist");
            } else if (newUser.getName().trim().equals(userList.get(i).getName())){
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

    public ResponseEntity Login (UserLoginDTO user, HttpServletResponse httpServletResponse, ServletWebRequest request) {
        Map<String,String> errorMap = new HashMap<>();
        String status;

        if (repository.existsByEmail(user.getEmail())) {
            User userdb = repository.findByEmail(user.getEmail());
            if (argon2PasswordEncoder.matches(user.getPassword(), userdb.getPassword())) {
                errorMap.put("message", "Password Matched");
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                status = HttpStatus.OK.toString();
            } else {
                errorMap.put("message", "Password NOT Matched");
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                status = HttpStatus.UNAUTHORIZED.toString();
            }
        } else {
            errorMap.put("message", "A user with the specified email DOES NOT exist");
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            status = HttpStatus.NOT_FOUND.toString();
        }
        HandleValidationErrors errors = new HandleValidationErrors(
                Date.from(Instant.now()),
                httpServletResponse.getStatus(),
                request.getRequest().getRequestURI(),
                status,
                errorMap.get("message"));
        return ResponseEntity.status(httpServletResponse.getStatus()).body(errors);
    }
}
