package com.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponse {

    private Long boardId;         // 게시글 ID
    private String title;         // 제목
    private String content;       // 내용
    private Long userId;          // 작성자 ID
    private String imageUrl;      // 이미지 URL
    private String imageNm;       // 이미지 이름
    private LocalDateTime regDt;  // 등록 일시
    private LocalDateTime chgDt;  // 수정 일시
}