package com.envifo_backend_java.Envifo_backend_java.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private final JwtTokenFactory jwtTokenFactory;

    @Autowired
    public JwtUtils(JwtTokenFactory jwtTokenFactory) {
        this.jwtTokenFactory = jwtTokenFactory;
    }

    public String refreshToken(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return jwtTokenFactory.generateTokenFromDetails(userDetails);
        } catch (Exception e) {
            logger.error("Error al refrescar token: " + e.getMessage());
            throw new RuntimeException("Error interno al refrescar token");
        }
    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T getClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload());
    }

    public String getUsernameFromJWT(String token) {
        return getClaims(token, claims -> claims.get("email", String.class));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token mal formado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token no soportado: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Token vacío: " + e.getMessage());
        } catch (SignatureException e) {
            logger.error("Error en la firma: " + e.getMessage());
        }
        return false;
    }
}
