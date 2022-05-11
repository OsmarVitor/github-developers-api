package com.github.developers.config.security;

import com.github.developers.model.dto.UserLoginResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Calendar;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class JwtManager {

  public UserLoginResponseDTO createToken(String email, List<String> roles) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, SecurityConstants.JWT_EXPIRATION_DAYS);

    String jwtToken =
        Jwts.builder()
            .setSubject(email)
            .setExpiration(calendar.getTime())
            .claim(SecurityConstants.JWT_ROLE_KEY, roles)
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.API_KEY.getBytes())
            .compact();

    Long expiredIn = calendar.getTimeInMillis();

    return new UserLoginResponseDTO(jwtToken, expiredIn, SecurityConstants.JWT_PROVIDER);
  }

  public Claims parseToken(String jwtToken) throws JwtException {
    return Jwts.parser()
        .setSigningKey(SecurityConstants.API_KEY.getBytes())
        .parseClaimsJws(jwtToken)
        .getBody();
  }
}
