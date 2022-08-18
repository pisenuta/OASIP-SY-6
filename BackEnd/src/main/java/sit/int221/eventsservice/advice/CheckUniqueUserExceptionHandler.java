package sit.int221.eventsservice.advice;

public class CheckUniqueUserExceptionHandler extends Exception{
    public CheckUniqueUserExceptionHandler(String message){
        super(message);
    }
}
