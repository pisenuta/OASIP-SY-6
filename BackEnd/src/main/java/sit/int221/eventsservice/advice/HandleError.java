package sit.int221.eventsservice.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class HandleError {
    private Date timestamp = new Date();
    private Integer status;
    private String path;
    private String message;
    private String error;
    private Map<String,String> filedErrors;
}

//@Getter
//@Setter
//@AllArgsConstructor
//@ToString
//public class HandleError {
//    // customizing timestamp serialization format
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
//    private Date timestamp;
//
//    private int code;
//
//    private String status;
//
//    private String message;
//
//    private String stackTrace;
//
//    private Object data;
//
//    private Map<String,String> filedErrors;
//
//    public HandleError() {
//        timestamp = new Date();
//    }
//
//    public HandleError(
//            HttpStatus httpStatus,
//            String message
//    ) {
//        this();
//
//        this.code = httpStatus.value();
//        this.status = httpStatus.name();
//        this.message = message;
//    }
//
//    public HandleError(
//            HttpStatus httpStatus,
//            String message,
//            String stackTrace
//    ) {
//        this(
//                httpStatus,
//                message
//        );
//
//        this.stackTrace = stackTrace;
//    }
//
//    public HandleError(
//            HttpStatus httpStatus,
//            String message,
//            String stackTrace,
//            Object data
//    ) {
//        this(
//                httpStatus,
//                message,
//                stackTrace
//        );
//
//        this.data = data;
//    }
//}
