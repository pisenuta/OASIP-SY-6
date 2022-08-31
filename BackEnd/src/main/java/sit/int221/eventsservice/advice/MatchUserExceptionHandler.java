package sit.int221.eventsservice.advice;

import org.springframework.http.HttpStatus;

public class MatchUserExceptionHandler extends Exception{
    public MatchUserExceptionHandler(HttpStatus ok, String message){
        super(message);
    }
}
