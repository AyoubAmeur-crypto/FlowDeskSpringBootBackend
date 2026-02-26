package spring.tuto.flowdesk.jwt.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.session.MapSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.cookieName}")
    private String jwtCookie;

    @Value("${spring.app.jwtExpirationMs}")
    private Long jwtExpirationMs;





    @Bean
    public SessionRepository<MapSession> sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }
    public String getJwtFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        logger.debug("=== JWT Cookie Debug ===");
        logger.debug("Looking for cookie: '{}'", jwtCookie);
        logger.debug("Total cookies in request: {}", cookies != null ? cookies.length : 0);

        if (cookies != null) {
            for (Cookie c : cookies) {
                logger.debug("Found cookie: name='{}', value length={}, path='{}'",
                        c.getName(),
                        c.getValue() != null ? c.getValue().length() : 0,
                        c.getPath());

                // Find the cookie with actual JWT value (not empty)
                if (jwtCookie.equals(c.getName()) && c.getValue() != null && !c.getValue().isEmpty()) {
                    logger.debug("✓ Found valid '{}' cookie with JWT!", jwtCookie);
                    return c.getValue();
                }
            }
        }

        logger.warn("✗ Cookie '{}' NOT FOUND or empty in request!", jwtCookie);
        return null;
    }

    public ResponseCookie generateJwtCookie(UserDetailsImplt userDetailsImplt){
        String jwt = generateJwtTokenFromEmail(userDetailsImplt);

        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .secure(false)            // false for localhost (no HTTPS)
                .sameSite("Lax")          // Lax works for same-site navigation
                .build();

        return cookie;
    }

    public ResponseCookie generateCleanJwtCookie(){

        ResponseCookie cookie = ResponseCookie.from(jwtCookie, "")
                .path("/")
                .maxAge(0)                // Delete cookie
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .build();

        return cookie;
    }

    public String generateJwtTokenFromEmail(UserDetailsImplt userDetails) {

        String email = userDetails.getEmail();



        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            Jwts.parser()
                    .verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
