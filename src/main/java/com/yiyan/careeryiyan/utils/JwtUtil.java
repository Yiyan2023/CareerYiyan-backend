package com.yiyan.careeryiyan.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class JwtUtil {
    public static final String SECRET_KEY = "ai_draw";
    private static final long EXPIRATION_TIME_MILLIS = 24 * 60 * 60 * 1000;


    public static String generateToken(String id) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS);
        return Jwts.builder()
                .setSubject(id)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}