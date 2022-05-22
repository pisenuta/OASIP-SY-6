package sit.int221.eventsservice.advice;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.eventsservice.advice.HandleError;

import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler extends Exception{
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HandleError handleInvalidArgument(MethodArgumentNotValidException ex) {
        HandleError errors = new HandleError();
        Map<String, String> errorMap = new HashMap<>();
        errors.setStatus(400);
        errors.setPath("/api/events");
        errors.setMessage("Bad Request");
        errors.setError("Validation failed");
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMap.put(fieldName, errorMessage);
        });
        errors.setFiledErrors(errorMap);
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
    public HandleCheckUnique handleCheckUnique(DataIntegrityViolationException de) {
        HandleCheckUnique errors = new HandleCheckUnique();
        errors.setStatus(500);
        errors.setPath("/api/eventcategory");
        errors.setMessage("Internal Server Error");
        errors.setError("Clinic name must be unique.");
        return errors;
    }
}
