package sit.int221.eventsservice.config;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        final Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("message", "Invalid token");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        errors.put("timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        errors.put("status", response.getStatus());
        errors.put("error", "UNAUTHORIZED");
        errors.put("message", request.getAttribute("Errors"));
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), errors);
        }catch (Exception e){
            throw new ServletException();
        }

//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
