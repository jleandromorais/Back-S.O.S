package br.com.sosescolar.Config;

//      AQUI ESTAVA O ERRO PRINCIPAL     //
// import lombok.Value;                 // <<< ERRADO
import org.springframework.beans.factory.annotation.Value; // <<< CORRIGIDO

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

// --- IMPORTS QUE ESTAVAM FALTANDO ---
// Você precisa deles para o Jwts, Claims, etc. funcionarem

import java.util.function.Function;
// --- FIM DOS IMPORTS FALTANTES ---

import java.util.Date;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }
    public Date extractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public String generateToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername());
    }
    private String createToken(String subject) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}