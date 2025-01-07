package com.board.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class PasswordEncoderUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordEncoderUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // NOTE : 비밀번호 암호화
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // NOTE : 비밀번호 검증
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    

    // NOTE : Base64 디코딩
    public static String decodeBase64(String value) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(value);
            return new String(decodedBytes);
        } catch (IllegalArgumentException e) {
            System.out.println("Error during Base64 decoding: " + e.getMessage());
            return "";
        }
    }
}