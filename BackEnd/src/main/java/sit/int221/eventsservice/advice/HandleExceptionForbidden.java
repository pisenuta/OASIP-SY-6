package sit.int221.eventsservice.advice;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionForbidden extends Exception{
    public HandleExceptionForbidden(String message){
        super(message);
    }

}