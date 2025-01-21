package com.board.web.controller;

import com.board.dto.BoardListResponse;
import com.board.dto.BoardRequest;
import com.board.dto.BoardResponse;
import com.board.entity.Board;
import com.board.service.BoardService;
import com.board.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
            Map<String,Object> response = boardService.getBoardList(currentPage, searchKey, searchValue);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching board list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching board list");
        }
    }

    @PostMapping
    public ResponseEntity<?> addBoard(HttpServletRequest request, @RequestBody BoardRequest boardRequest) {
        try {
        	Long userId = JwtUtil.getUserIdFromSecurityContext();
        	Map<String, Object> response = boardService.addBoard(boardRequest, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding board post");
        }
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoardInfo(@PathVariable Long boardId, HttpServletRequest request) {
        try {
            // JWT에서 사용자 ID 가져오기
            Long userId = JwtUtil.getUserIdFromSecurityContext();
            
            // Header에서 CurrentPage 가져오기
            String referrer = request.getHeader("CurrentPage");
            String url = null;
            if (referrer != null) {
                try {
                    URI referrerUri = new URI(referrer);
                    url = referrerUri.getPath().split("/")[1]; // URL에서 첫 번째 경로 추출
                } catch (Exception e) {
                    System.err.println("Invalid referrer URL: " + referrer);
                }
            }
            // 게시글 정보 가져오기
            Map<String, Object> response = boardService.getBoardInfo(boardId, userId, url);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching board info: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "server error", "data", null));
        }
    }


    @PutMapping("/{boardId}")
    public ResponseEntity<?> editBoard(HttpServletRequest request,
        @PathVariable Long boardId, @RequestBody BoardRequest boardRequest
    ) {
        try {
        	Long userId = JwtUtil.getUserIdFromSecurityContext();
        	boardRequest.setUserId(userId);
        	
        	Map<String, Object> response = boardService.editBoard(boardId, boardRequest);
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
    
    @PatchMapping("/view/{boardId}")
    public ResponseEntity<?> addviewBoard(HttpServletRequest request, @PathVariable Long boardId
    ) {
        try {
        	Long userId = JwtUtil.getUserIdFromSecurityContext();
        	
            int result = boardService.addViewBoard(boardId, userId);
        	return ResponseEntity.ok(result);            	

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating board post");
        }
    }
}