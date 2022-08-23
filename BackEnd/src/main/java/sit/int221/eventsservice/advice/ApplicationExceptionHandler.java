package sit.int221.eventsservice.advice;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler extends Exception {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HandleError handleInvalidArgument(MethodArgumentNotValidException ex, ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> {
                    errorMap.put(error.getField(), error.getDefaultMessage());
                }
        );
        HandleError errors = new HandleError(
                Date.from(Instant.now()), HttpStatus.BAD_REQUEST.value(), request.getRequest().getRequestURI(), "Validation failed",
                "Bad request", errorMap);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OverlappedExceptionHandler.class)
    public HandleOverlappedError handleOverlapped(OverlappedExceptionHandler rs) {
        HandleOverlappedError errors = new HandleOverlappedError();
        errors.setStatus(400);
        errors.setPath("/api/events");
        errors.setMessage("Bad Request");
        errors.setError("Time is Overlapped");
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", rs.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public HandleCheckUnique handleCheckUnique(DataIntegrityViolationException de, ServletWebRequest request) {
        HandleCheckUnique errors = new HandleCheckUnique();
        errors.setStatus(500);
        errors.setPath("/api/eventcategory/"+ request.getRequest().getRequestURI());
        errors.setMessage("Internal Server Error");
        errors.setError("Clinic name must be unique.");
        return errors;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CheckUniqueUserExceptionHandler.class)
    public HandleCheckUnique handleCheckUniqueUser(CheckUniqueUserExceptionHandler cu, ServletWebRequest request) {
        HandleCheckUnique errors = new HandleCheckUnique();
        String mser = cu.getMessage();
        errors.setStatus(500);
        errors.setPath(request.getRequest().getRequestURI());
        errors.setMessage("Internal Server Error");
        errors.setError(mser);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public HandleCheckUnique handleNotEnum(HttpMessageNotReadableException ex, ServletWebRequest request) {
        HandleCheckUnique errors = new HandleCheckUnique();
        errors.setStatus(400);
        errors.setPath("/api/users");
        errors.setMessage("Bad Request");
        errors.setError("Role is wrong.");
        return errors;
    }

}
