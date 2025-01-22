package com.board.security;

import com.board.entity.auth.JwtUserDetails;
import com.board.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();                
    	if (requestUri.startsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
            
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // "Bearer " 제거

            try {
                // JWT 검증
                Claims claims = jwtUtil.validateToken(token);
                String email = claims.getSubject();  // 토큰의 subject에서 email 추출
                Long userIdFromToken = claims.get("userId", Long.class);  // 토큰에서 userId 추출
                
                // 요청 경로의 userId 추출
                String requestUri = request.getRequestURI();  // 예: "/users/3"
                Long userIdFromPath = extractUserIdFromPath(requestUri);  // 경로에서 userId 추출
                
                // 토큰의 userId와 요청 경로의 userId 비교
                if (userIdFromPath != null && !userIdFromPath.equals(userIdFromToken)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Forbidden: You can only modify your own data");
                    return;
                }

                // 인증 객체 생성 및 SecurityContextHolder 설정
                JwtUserDetails userDetails = new JwtUserDetails(userIdFromToken, email, isAdmin, nickname);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                		userDetails, null, null
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Long extractUserIdFromPath(String requestUri) {
        // URL에서 "/users/{id}" 형태의 ID 부분 추출
        if (requestUri.matches("/users/\\d+")) {
            return Long.parseLong(requestUri.substring(requestUri.lastIndexOf("/") + 1));
        }
        return null;
    }

}
