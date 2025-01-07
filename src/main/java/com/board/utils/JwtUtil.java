package com.board.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.board.entity.User;
import com.board.entity.auth.JwtUserDetails;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    //private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 랜덤 키 생성
	//private final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // 최소 256비트
	private final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // 최소 256비트
	private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    // JWT 생성 메서드
    public String generateToken(Long userId, String email) {
    	String jwt = Jwts.builder()
                .setSubject(email) // 이메일을 주제로 설정
                .claim("userId", userId) // user_id를 클레임에 추가
                .setIssuedAt(new Date()) // 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(key) // 서명
                .compact();
    	return jwt;
    }

    // JWT 검증 및 클레임 파싱 메서드
    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("JWT Token has expired", e);
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("JWT Token is unsupported", e);
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException("JWT Token is invalid", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("JWT Token signature is invalid", e);
        } catch (JwtException e) {
            throw new IllegalArgumentException("JWT Token is invalid", e);
        }
    }
    
    public static Long getUserIdFromSecurityContext() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JwtUserDetails) {
        	JwtUserDetails userDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getId();
        }
        return null;  // 인증 정보가 없을 경우 null 반환
    }
}
