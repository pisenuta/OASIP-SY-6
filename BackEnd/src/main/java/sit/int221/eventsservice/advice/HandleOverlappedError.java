package sit.int221.eventsservice.advice;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HandleOverlappedError {
        private Date timestamp = new Date();
        private Integer status;
        private HttpStatus message;
        private String path;
        private String error;
}

