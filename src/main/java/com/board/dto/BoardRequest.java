package com.board.dto;

public class BoardRequest {

    private String title;      // NOTE : 게시글 제목
    private String content;    // NOTE : 게시글 내용
    private String imageUrl;   // NOTE : 이미지 URL
    private String imageNm;    // NOTE : 이미지 이름
    private String email;      // NOTE : 사용자 이메일
    private Long userId;       // NOTE : 사용자 ID

    // NOTE : 기본 생성자
    public BoardRequest() {}

    // NOTE : 모든 필드를 포함한 생성자
    public BoardRequest(String title, String content, String imageUrl, String imageNm, String email, Long userId) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.imageNm = imageNm;
        this.email = email;
        this.userId = userId;
    }

    // NOTE : Getter 메서드
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageNm() {
        return imageNm;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserId() {
        return userId;
    }

    // NOTE : Setter 메서드 (필요하면 추가)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageNm(String imageNm) {
        this.imageNm = imageNm;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
