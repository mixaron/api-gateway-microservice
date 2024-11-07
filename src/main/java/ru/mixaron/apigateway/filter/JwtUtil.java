package ru.mixaron.apigateway.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {


    public static final String SECRET = "s7gH1MvYz@HsG98M4n2D3FgH9Z2r1J6Kz2S8d9H0J";


    public void validateToken(final String token) {
        Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))  // Без декодирования BASE64
                .build()
                .parseClaimsJws(token);
    }
}