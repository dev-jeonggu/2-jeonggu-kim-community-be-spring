package com.board.web.controller;

import com.board.dto.BoardListResponse;
import com.board.dto.BoardRequest;
import com.board.dto.BoardResponse;
import com.board.entity.Board;
import com.board.service.BoardService;
import com.board.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor // NOTE : 생성자를 통해 의존성 주입
@Slf4j
public class BoardController {
	// NOTE : final 키워드를 사용하면 해당 필드가 초기화 후 수정되지 않음을 보장
    private final BoardService boardService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getBoardList(
        @RequestParam(defaultValue = "1") int currentPage,
        @RequestParam(defaultValue = "") String searchKey,
        @RequestParam(defaultValue = "") String searchValue
    ) {
        try {
            BoardListResponse response = boardService.getBoardList(currentPage, searchKey, searchValue);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching board list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching board list");
        }
    }

    @PostMapping
    public ResponseEntity<?> addBoard(HttpServletRequest request, @RequestBody BoardRequest boardRequest) {
        try {
            BoardResponse response = boardService.addBoard(boardRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding board post");
        }
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardInfo(@PathVariable Long boardId) {
        try {
            BoardResponse response = boardService.getBoardInfo(boardId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Board not found");
        }
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<?> editBoard(HttpServletRequest request,
        @PathVariable Long boardId, @RequestBody BoardRequest boardRequest
    ) {
        try {
        	Long userId = JwtUtil.getUserIdFromSecurityContext();
        	boardRequest.setUserId(userId);
        	
            BoardResponse response = boardService.editBoard(boardId, boardRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating board post");
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(HttpServletRequest request, @PathVariable Long boardId) {
        try {
        	Long userId = JwtUtil.getUserIdFromSecurityContext();
            boardService.deleteBoard(boardId, userId);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting board");
        }
    }
}