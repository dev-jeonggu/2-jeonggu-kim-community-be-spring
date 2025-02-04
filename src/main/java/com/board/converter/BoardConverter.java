package com.board.converter;

import org.springframework.stereotype.Component;

import com.board.dto.BoardRequest;
import com.board.dto.BoardResponse;
import com.board.entity.Board;
import com.board.entity.User;

@Component
public class BoardConverter {

    public BoardResponse toDto(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .userId(board.getUserId())
                .imageUrl(board.getImageUrl())
                .imageNm(board.getImageNm())
                .regDt(board.getRegDt())
                .chgDt(board.getChgDt())
                .build();
    }

    public Board toEntity(BoardRequest dto) {
        User user = User.builder()
                .userId(dto.getUserId())
                .build();

        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .imageUrl(dto.getImageUrl())
                .imageNm(dto.getImageNm())
                .build();
    }
}
