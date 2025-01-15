package com.board.entity.auth;

public class JwtUserDetails {
    private final Long userId;
    private final String email;
    private final String isAdmin;
    private final String nickname;

    public JwtUserDetails(Long userId, String email, String isAdmin, String nickname) {
        this.userId = userId;
        this.email = email;
        this.isAdmin = isAdmin;
        this.nickname = nickname;
    }

    public Long getId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
    
    public String getIsAdmin() {
        return isAdmin;
    }
    public String getNickname() {
        return nickname;
    }
}