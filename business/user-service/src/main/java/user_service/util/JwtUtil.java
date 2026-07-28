package user_service.util;

import com.umar.exceptions.common.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import user_service.constants.JwtConstant;
import user_service.model.User;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private SecretKey key;
    private long accessTokenExpiration=9000000;
    private long refreshTokenExpiration=604800000;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        this.key =Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    }

    public String generateAccessToken(User user){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId",user.getId())
                .claim("role",user.getRole().name())
                .claim("email",user.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(User  user){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId",user.getId())
                .claim("type","refresh")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String hashRefreshToken(String refreshToken) {
        return passwordEncoder.encode(refreshToken);
    }

    public boolean matchesRefreshToken(String rawToken, String hashedToken) {
        return passwordEncoder.matches(rawToken, hashedToken);
    }
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        }
        catch (SecurityException e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "user.jwt.invalidJwt");
        } catch (Exception e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "user.jwt.invalidToken");
        }
    }

    public String extractUserEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String getAccessTokenExpiry(String token){
        Date expiration = extractClaims(token).getExpiration();
        return expiration.toString();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return !isTokenExpired(token);
        } catch (Exception ex) {
            return false;
        }
    }

    public long getRemainingExpiration(String token) {
        Date expiration =
                extractClaims(token).getExpiration();

        long remaining =
                expiration.getTime() -
                        System.currentTimeMillis();

        return Math.max(remaining, 0);
    }

    public boolean validateToken(String token, UserDetails userDetails,String userName){
        if(validateToken(token)){
            return userDetails.getUsername().equals(userName);
        }
        return false;
    }



    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for(GrantedAuthority authority:authorities){
            auths.add(authority.getAuthority());
        }
        return String.join(",",auths);

    }
}
