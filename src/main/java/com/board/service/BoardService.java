package com.board.service;

import com.board.dto.BoardListResponse;
import com.board.dto.BoardRequest;
import com.board.dto.BoardResponse;
import com.board.repo.BoardRepository;
import com.board.repo.admin.NotificationRepository;
import com.board.utils.LoggerUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // NOTE : 게시글 목록 조회
    public BoardListResponse getBoardList(int currentPage, String searchKey, String searchValue) {
        int offset = (currentPage - 1) * 5; // NOTE : 페이지네이션 계산
        int limit = 5;
        List<Map<String, Object>> boardList = boardRepository.findAllBySearch(searchKey, searchValue, offset, limit);
        boolean hasNext = boardList.size() == limit; // NOTE : 다음 페이지가 있는지 확인

        return new BoardListResponse(boardList, hasNext);
    }

    // NOTE : 게시글 추가
    public BoardResponse addBoard(BoardRequest request) {
        int result = boardRepository.addBoard(
            request.getTitle(),
            request.getContent(),
            request.getUserId(),
            request.getImageUrl(),
            request.getImageNm()
        );

        if (result > 0) {        
            
            return new BoardResponse("Board added successfully");
        } else {
            throw new RuntimeException("Failed to add board");
        }
    }

    // NOTE : 게시글 상세 조회
    public BoardResponse getBoardInfo(Long boardId) {
        Map<String, Object> board = boardRepository.findById(boardId);

        if (board.isEmpty()) {
            throw new RuntimeException("Board not found");
        }

        return new BoardResponse(board);
    }

    // NOTE : 게시글 수정
    public BoardResponse editBoard(Long boardId, BoardRequest request) {
        int result = boardRepository.editBoard(
            boardId,
            request.getTitle(),
            request.getContent(),
            request.getImageUrl(),
            request.getImageNm(),
            request.getUserId()
        );

        if (result > 0) {
        	return new BoardResponse("Board updated successfully");
        } else {
            throw new RuntimeException("Failed to update board");
        }
    }

    // NOTE : 게시글 삭제
    public void deleteBoard(Long boardId, Long userId) {
        int result = boardRepository.deleteBoard(boardId, userId);
        	LoggerUtil.debug("Board not found");
    }
}
