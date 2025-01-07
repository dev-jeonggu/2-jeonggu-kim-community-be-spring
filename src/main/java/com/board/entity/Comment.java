package com.board.entity;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

public class Comment {

    private Long commentId; // comment_id
    private Long boardId;   // 게시글 ID
    private Long userId;    // 사용자 ID
    private String content; // 댓글 내용
    private String profileUrl; // 프로필 URL
    private String nickname;   // 닉네임
    @Nullable
    private LocalDateTime regDt = LocalDateTime.now();; // 등록일시
    @Nullable
    private LocalDateTime chgDt = null; // 수정일시

    // isAuthor와 isChanged는 추가 데이터로 사용
    private boolean isAuthor;  // 작성자인지 여부
    private boolean isChanged; // 수정되었는지 여부

    // Getters and Setters
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
