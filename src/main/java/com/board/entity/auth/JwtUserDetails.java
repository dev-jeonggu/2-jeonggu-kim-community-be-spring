package com.board.entity.auth;

public class JwtUserDetails {
    private final Long userId;
    private final String email;

    public JwtUserDetails(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public Long getId() {  // `id` 이름으로 반환하도록 메서드 추가
        return userId;
    }

    public String getEmail() {
        return email;
    }
}