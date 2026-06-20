package com.umar.util;

import com.umar.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {




    private SecretKey key;
    private String  test="hdcbhjevbhje3yudsbhrvechvagrcdbxhg23ugcxvwhqf4yghcv";



    @Value("${jwt.secret:hdcbhjevbhje3yudsbhrvechvagrcdbxhg23ugcxvwhqf4yghcv}")
    private String secretKey;


    @PostConstruct
    public void init() {
        System.out.println("====== JWT PROPERTIES INITIALIZATION START ======");
        System.out.println("Property Injected String: " + this.secretKey);
        System.out.println("String Length: " + this.secretKey.length());

        // Always use the instance variable here
        this.key = Keys.hmacShaKeyFor(this.secretKey.getBytes());
        System.out.println("Generated Key: " + this.key);
        System.out.println("====== JWT PROPERTIES INITIALIZATION END ======");
    }


    public boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("key is null : ---- "+token);
            extractClaims(token);
            return !isTokenExpired(token);
        } catch (Exception ex) {
            return false;
        }
    }

    public Claims extractClaims(String token) {
        this.key = Keys.hmacShaKeyFor(this.test.getBytes());
        System.out.println("key is null :-    [-----"+key);
        System.out.println("token is null :-    [-----"+token);
        System.out.println("key is null : ---- "+test);


        return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();


    }

}
