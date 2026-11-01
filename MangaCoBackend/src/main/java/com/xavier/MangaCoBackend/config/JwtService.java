package com.xavier.MangaCoBackend.config;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

/**
 * Servicio JWT.
 * Se encarga de generar, extraer información y validar tokens JWT usando la librería jjwt (0.12.x).
 *
 * jjwt 0.12.x por debajo:
 *   - Jwts.builder()           → construye el JWT (subject, fechas, firma)
 *   - signWith(key, alg)       → firma el token con una clave secreta y algoritmo HMAC-SHA256
 *   - compact()                → serializa el JWT a String (header.payload.signature)
 *   - Jwts.parser().verifyWith(key).build() → crea un parser verificador de firma
 *   - parseSignedClaims(token) → valida firma y expiración, devuelve el cuerpo (claims)
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * Pasos internos de jjwt 0.12.x:
     *   1. Jwts.builder() crea el constructor del JWT
     *   2. .subject(username) establece el "sub" (subject) con el username
     *   3. .issuedAt(fecha) establece la fecha de emisión (iat)
     *   4. .expiration(fecha) establece la fecha de expiración (exp)
     *   5. .signWith(clave, HS256) firma el token con HMAC-SHA256
     *   6. .compact() serializa el JWT a su forma final String
     *
     * @param userDetails usuario autenticado (implementa UserDetails)
     * @return String JWT listo para enviarse al cliente
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Extrae el username (subject) de un token JWT.
     *
     * jjwt 0.12.x por debajo:
     *   - parseSignedClaims(token) valida la firma y extrae los claims
     *   - getPayload().getSubject() obtiene el campo "sub"
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Valida si un token es correcto para un usuario dado.
     *
     * jjwt 0.12.x por debajo:
     *   - parseSignedClaims(token) lanza excepción si la firma es inválida o expiró
     *   - Comparamos el username extraído con el del UserDetails
     *   - Verificamos que el token no esté expirado
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /* ---------- Métodos privados auxiliares ---------- */

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, claims -> claims.getExpiration());
    }

    /**
     * Convierte la clave secreta (String) en una SecretKey criptográfica.
     * jjwt necesita una clave de bytes para firmar/verificar con HMAC.
     * Usamos Decoders.BASE64 para decodificar la clave y Keys.hmacShaKeyFor
     * para crear la clave criptográfica.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
