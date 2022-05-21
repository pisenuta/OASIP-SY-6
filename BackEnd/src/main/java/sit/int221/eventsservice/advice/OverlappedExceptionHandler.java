package sit.int221.eventsservice.advice;

public class OverlappedExceptionHandler extends Exception{
    public OverlappedExceptionHandler(String message){
        super(message);
    }
}
