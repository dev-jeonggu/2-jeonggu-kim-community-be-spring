package com.board.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

import com.board.entity.Board;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    private Long boardId;
    private String title;
    private String content;
    private String email;
    private String imageUrl;
    private String imageNm;
    private LocalDateTime regDt;
    private LocalDateTime chgDt;

    public BoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.email = board.getEmail();
        this.imageUrl = board.getImageUrl();
        this.imageNm = board.getImageNm();
        this.regDt = board.getRegDt();
        this.chgDt = board.getChgDt();
    }

	public BoardResponse(String string) {
		// TODO Auto-generated constructor stub
	}

	public BoardResponse(Map<String, Object> board) {
		// TODO Auto-generated constructor stub
	}
}