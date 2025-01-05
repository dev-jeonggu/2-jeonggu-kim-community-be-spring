package com.board.service;

import com.board.entity.User;
import com.board.repo.AuthRepository;
import com.board.repo.UserRepository;
import com.board.utils.JwtUtil;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder; // 비밀번호 검증용

    @Autowired
    public AuthService(AuthRepository authRepository, JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Map<String, Object> login(String email, String password) {
        try {
            Map<String, Object> user = authRepository.findByEmail(email);

            if (user != null && !user.isEmpty()) {
                String storedPassword = (String) user.get("password");
            	
                if (passwordEncoder.matches(password, storedPassword)) {
                	Long userId = ((Number) user.get("user_id")).longValue();
                	String token = jwtUtil.generateToken(userId, email);
                	// 로그인 성공
                	Map<String, Object> result = Map.of(
                		    "success", true,
                		    "message", "로그인에 성공하였습니다.",
                		    "token", token,
                		    "user_id", user.get("user_id") != null ? ((Number) user.get("user_id")).longValue() : null,
                		    "email", user.get("email") != null ? user.get("email") : "",
                		    "nickname", user.get("nickname") != null ? user.get("nickname") : "",
                		    "profile_url", user.get("profile_url") != null ? user.get("profile_url") : ""
                		);
                    return result;
                } else {
                    // 비밀번호 불일치
                    return Map.of(
                        "success", false,
                        "message", "비밀번호가 틀렸습니다."
                    );
                }
            } else {
                // 이메일에 해당하는 사용자 없음
                return Map.of(
                    "success", false,
                    "message", "존재하지 않는 아이디입니다."
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("로그인 처리 중 오류가 발생했습니다.", e);
        }
    }
}