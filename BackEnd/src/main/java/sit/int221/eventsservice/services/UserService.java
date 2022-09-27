package sit.int221.eventsservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.*;
import sit.int221.eventsservice.dtos.User.UserCreateDTO;
import sit.int221.eventsservice.dtos.User.UserDTO;
import sit.int221.eventsservice.dtos.User.UserLoginDTO;
import sit.int221.eventsservice.entities.User;
import sit.int221.eventsservice.models.JwtResponse;
import sit.int221.eventsservice.repositories.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private Argon2PasswordEncoder argon2PasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public List<UserDTO> getAllUser() {
        return this.listMapper.mapList(this.repository.findAll(Sort.by("name").ascending()), UserDTO.class, this.modelMapper);
    }

    public UserDTO getUserById(Integer id) {
        User user = this.repository.findById(id).orElseThrow(() -> {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, id + " Does Not Exist !!!");
        });
        return this.modelMapper.map(user, UserDTO.class);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public User save(UserCreateDTO newUser) throws CheckUniqueUserExceptionHandler {
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

//    public ResponseEntity Login (UserLoginDTO user, HttpServletResponse httpServletResponse, ServletWebRequest request) throws Exception {
//        Map<String,String> errorMap = new HashMap<>();
//        String status;
//
//        if (repository.existsByEmail(user.getEmail())) {
//            User userdb = repository.findByEmail(user.getEmail());
//            if (argon2PasswordEncoder.matches(user.getPassword(), userdb.getPassword())) {
//                errorMap.put("message", "Password Matched");
//                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//                status = HttpStatus.OK.toString();
//
//                authenticate(user.getEmail() , user.getPassword());
//                authenticate(user.getEmail(), user.getPassword());
//
//                final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
//
//                final String token = jwtTokenUtil.generateToken(userDetails);
//                final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
//
//                return ResponseEntity.ok(new JwtResponse("Liogin Successfully", token, refreshToken));
//
//            } else {
//                errorMap.put("message", "Password NOT Matched");
//                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                status = HttpStatus.UNAUTHORIZED.toString();
//            }
//        } else {
//            errorMap.put("message", "A user with the specified email DOES NOT exist");
//            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            status = HttpStatus.NOT_FOUND.toString();
//        }
//        HandleValidationErrors errors = new HandleValidationErrors(
//                Date.from(Instant.now()),
//                httpServletResponse.getStatus(),
//                request.getRequest().getRequestURI(),
//                status,
//                errorMap.get("message"));
//        return ResponseEntity.status(httpServletResponse.getStatus()).body(errors);
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if(user == null){
            System.out.println("Email not found in the database: " + email);
            throw new UsernameNotFoundException("Email not found in the database: " + email);
        }else {
            System.out.println("Email found in the database: " + email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public User getUserByEmail(String email) {
        User userByEmail = repository.findByEmail(email);
        return modelMapper.map(userByEmail, User.class);
    }
}
