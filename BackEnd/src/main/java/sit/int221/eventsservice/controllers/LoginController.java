package sit.int221.eventsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import sit.int221.eventsservice.advice.MatchUserExceptionHandler;
import sit.int221.eventsservice.dtos.User.UserLoginDTO;
import sit.int221.eventsservice.services.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({"/api/login"})
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity Login(@Valid @RequestBody UserLoginDTO user, HttpServletResponse HttpServletResponse, ServletWebRequest request) throws Exception {
        return userService.Login(user , HttpServletResponse , request);
    }
}
