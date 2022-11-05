package sit.int221.eventsservice.advice;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HandleErrorUnsuccessful {
    private String timestamp;
    private Integer status;
    private String path;
    private String message;
    private String error;
    private Map<String,String> filedErrors;
}
