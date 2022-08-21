package sit.int221.eventsservice.advice;

import org.springframework.web.context.request.WebRequest;

public class CheckUniqueUserExceptionHandler extends Exception{
    public CheckUniqueUserExceptionHandler(String message){
        super(message);
    }
}
