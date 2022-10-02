package sit.int221.eventsservice.advice;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class HandleExceptionBadRequest extends Exception{
    public HandleExceptionBadRequest(String message){
        super(message);
    }

}