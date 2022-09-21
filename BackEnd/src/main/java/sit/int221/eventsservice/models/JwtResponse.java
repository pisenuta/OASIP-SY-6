package sit.int221.eventsservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
//    private final String jwttoken;
    private String message;
    private String accessToken;
    private String refreshToken;

    public JwtResponse(String message, String accessToken, String refreshToken) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}