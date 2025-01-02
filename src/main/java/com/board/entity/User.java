package com.board.entity;

import java.time.LocalDateTime;

public class User {

    private Long id; // user_id
    private String email;
    private String password;
    private String nickname;
    private String profileUrl;
    private LocalDateTime regDt; // 등록일시
    private LocalDateTime chgDt; // 수정일시

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
    
    public LocalDateTime getRegDt() {
        return regDt;
    }

    public void setRegDt(LocalDateTime regDt) {
        this.regDt = regDt;
    }
    
    public LocalDateTime getChgDt() {
        return chgDt;
    }

    public void setChgDt(LocalDateTime chgDt) {
        this.chgDt = chgDt;
    }
}