package sit.int221.eventsservice.services;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sit.int221.eventsservice.config.JwtTokenUtil;
import sit.int221.eventsservice.models.JwtResponse;
import sit.int221.eventsservice.models.Response;

import javax.servlet.http.HttpServletRequest;

@Service
public class RefreshTokenService {
    private final JwtTokenUtil jwtTokenUtill;
    private final JwtUserDetailsService jwtUserDetailsService;

    public RefreshTokenService(JwtTokenUtil jwtTokenUtill, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtill = jwtTokenUtill;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    public ResponseEntity refreshToken(HttpServletRequest request){
        String requestRefreshToken = request.getHeader("Authorization").substring(7);
        String userRefreshToken = jwtTokenUtill.getUsernameFromToken(requestRefreshToken);
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userRefreshToken);
        String accessToken = jwtTokenUtill.generateToken(userDetails);
        String refreshToken = jwtTokenUtill.generateRefreshToken(userDetails);
//            String getRefreshTokenExpire = jwtTokenUtill.getExpirationDateFromToken(u)
        if (checkExpired(requestRefreshToken).equals(true)) {
            return ResponseEntity.ok(new JwtResponse("Refresh Token Successfully",accessToken,refreshToken));
        }
        return Response.response(HttpStatus.NOT_FOUND, "Refresh Token does not exist !!");
    }

    private Boolean checkExpired(String request){
        if(!jwtTokenUtill.isTokenExpired(request)){
            return true;
        }
        return false;
    }
}
