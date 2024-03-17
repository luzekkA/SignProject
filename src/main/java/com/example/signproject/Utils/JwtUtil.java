package com.example.signproject.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.signproject.entity.User;

import java.util.Calendar;
import java.util.Map;

public class JwtUtil {
    private static final String secretKey = "shuiwuLogin";

    public static String generateToken(User user) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("password", user.getPassword())
                .withExpiresAt(instance.getTime()) // 7 days expiration
                .sign(Algorithm.HMAC256(secretKey));
    }

    public static Map<String, Claim> getSubject(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secretKey))
                    //.withIssuer("your-issuer") // replace with your actual issuer
                    .build()
                    .verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
            throw new RuntimeException("Token validation failed", e);
        }
    }

    public static String getId(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        return jwt.getClaim("id").asString();
    }

}