package sit.int221.eventsservice.advice;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HandleUnexpectedType {
    private Date timestamp = new Date();
    private Integer status;
    private String message;
    private String path;
    private String error;
}
