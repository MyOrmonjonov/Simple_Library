package org.example.simple_library.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.example.simple_library.entity.Role;
import org.example.simple_library.entity.RoleName;
import org.example.simple_library.entity.Users;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import io.jsonwebtoken.Jwts;
@Service
public class JwtService {

    public  String generateToken(Users user){
        Map<String, Object> claims=new HashMap<>();
        claims.put("id",user.getId());
        claims.put("username",user.getUsername());
        claims.put("password",user.getPassword());
        claims.put("role",user.getRole().getRoleName().name());

        String token = Jwts.builder()
                .subject(user.getUsername())
                .claims(claims)
                .signWith(signKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .compact();
        return token;
    }

    private SecretKey signKey() {
        return Keys.hmacShaKeyFor(
                "01234567890123456789012345678901".getBytes());
    }


    public boolean isValid(String token) {
        try{
            getClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
        }

    public Role getRole(String token) {
        Claims claims = getClaims(token);
        String roleName = (String) claims.get("role");

        try {
            return new Role(RoleName.valueOf(roleName)); // faqat RoleName qo‘shildi
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token ichidagi rol noto'g'ri: " + roleName);
        }
    }




}
