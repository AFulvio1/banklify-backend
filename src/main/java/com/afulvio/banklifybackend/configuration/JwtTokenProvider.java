package com.afulvio.banklifybackend.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public String generateToken(String email, Long clientId) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("clientId", clientId)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public Claims getClaimsFromJWT(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }

    public String getEmailFromJWT(String token) {
        return getClaimsFromJWT(token).getSubject();
    }

    public Long getClientIdFromJWT(String token) {
        return getClaimsFromJWT(token).get("clientId", Long.class);
    }
}
