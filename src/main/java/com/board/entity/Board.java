package com.board.entity;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//@Data
//public class Board {
//    public Board(String title2, String content2, String email2) {
//		// TODO Auto-generated constructor stub
//	}
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long boardId;
//
//	@Column(nullable = false)
//    private String title;
//	@Column(nullable = false)
//    private String content;
//	@Column(nullable = false)
//    private String email;
//	@Column(nullable = false)
//    private String imageUrl;
//	@Column(nullable = false)
//    private String imageNm;
//	@Column(nullable = false)
//    private LocalDateTime regDt = LocalDateTime.now();
//	@Column(nullable = false)
//	private LocalDateTime chgDt;
//}

import lombok.Data;
import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

@Data
public class Board {

    private Long boardId;        // 게시글 ID
    private String title;        // 제목
    private String content;      // 내용
    private String email;        // 작성자 이메일
    private String nickname;
    private String imageUrl;     // 이미지 URL
    private String imageNm;      // 이미지 이름
    @Nullable
    private LocalDateTime regDt; // 등록일시
    @Nullable
    private LocalDateTime chgDt; // 수정일시

    // 기본 생성자
    public Board() {}

    // 생성자 (필요한 경우 추가)
    public Board(Long boardId, String title, String content, String email, String nickname, String imageUrl, String imageNm, LocalDateTime regDt, LocalDateTime chgDt) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.imageNm = imageNm;
        this.regDt = regDt;
        this.chgDt = chgDt;
    }
}