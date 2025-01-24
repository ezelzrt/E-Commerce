package ar.uba.fi.ingsoft1.auth.service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import ar.uba.fi.ingsoft1.exception.ExpiredTokenException;
import ar.uba.fi.ingsoft1.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.uba.fi.ingsoft1.user.User;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String extractUsername(final String token){
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public String generateToken(final User user){
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(final User user){
        return buildToken(user, refreshExpiration);
    }

    private String buildToken(final User user, final long expiration){
        return Jwts.builder()
                .id(user.getId().toString())
                .claims(Map.of(
                        "name", user.getFirstName(),
                        "userID", user.getId(),
                        "accessType", user.getAccessType()
                ))
                .subject(user.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(getSignInKey())
                .compact();
    }

    public void validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("Token is missing or empty");
        }

        if (!isValidToken(token)) {
            throw new InvalidTokenException("Token is invalid");
        }

        if (isTokenExpired(token)) {
            throw new ExpiredTokenException("Token has expired");
        }
    }

    private boolean isValidToken(final String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (SignatureException ex) {
            throw new InvalidTokenException("Token signature is invalid", ex);
        } catch (Exception ex) {
            throw new InvalidTokenException("Invalid token format", ex);
        }
    }
    private boolean isTokenExpired(final String token){
        return extractExpiration(token).before(Date.from(Instant.now()));
    }


    private Date extractExpiration(final String token){
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("userID", Long.class);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public Integer extractAccessType(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("accessType", Integer.class);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String validateAndExtractToken(String authorizationHeader) throws InvalidTokenException, ExpiredTokenException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Authorization header is required and must start with 'Bearer'.");
        }

        String token = authorizationHeader.replace("Bearer ", "");
        validateToken(token);
        return token;
    }
}
