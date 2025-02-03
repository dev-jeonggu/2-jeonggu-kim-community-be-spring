package com.board.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequest {

    private String title;       // 제목
    private String content;     // 내용
    private Long userId;        // 작성자 ID
    private String imageUrl;    // 이미지 URL
    private String imageNm;     // 이미지 이름
}
