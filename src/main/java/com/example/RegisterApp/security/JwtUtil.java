package com.example.RegisterApp.security;

    import com.example.RegisterApp.model.User;
    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Component;

    import javax.crypto.SecretKey;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.function.Function;

    @Component
    public class JwtUtil {

        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private long expiration;

        private SecretKey getSigningKey() {
            return Keys.hmacShaKeyFor(secret.getBytes());
        }

        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }

        private Claims extractAllClaims(String token) {
            return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        }

        private Boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        public String generateToken(User user) { // Cambiar parámetro a User
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("email", user.getEmail());
            claims.put("name", user.getName());
            claims.put("lastname", user.getLastName());
            claims.put("username", user.getUsername());
            //claims.put("password", user.getPassword()); // ¡Advertencia de seguridad!
            claims.put("status", user.isStatus());
            return createToken(claims, user.getUsername());
        }

        private String createToken(Map<String, Object> claims, String subject) {
            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
        }

        public Boolean validateToken(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
    }