package com.board.web.controller;

import com.board.dto.BoardListResponse;
import com.board.dto.BoardRequest;
import com.board.dto.BoardResponse;
import com.board.entity.Board;
import com.board.entity.Comment;
import com.board.service.BoardService;
import com.board.service.CommentService;
import com.board.utils.JwtUtil;
import com.board.utils.ResponseUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
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
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // NOTE : 댓글 추가
    @PostMapping
    public ResponseEntity<?> addComment(HttpServletRequest request, @RequestBody Map<String, Object> requestBody) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
        String nickname = JwtUtil.getNicknameFromSecurityContext();
    	Long boardId = requestBody.get("board_id") != null ? ((Number) requestBody.get("board_id")).longValue() : null;
        String content = (String) requestBody.get("content");
        if(boardId == null) {
        	ResponseEntity.status(500).body("Failed to add comment.");
        }
        Map<String,Object> result = commentService.addComment(boardId, content, userId, nickname);
        if(result == null) {
        	return ResponseEntity.status(500).body("Failed to add comment.");
        }else {
        	return ResponseEntity.ok(ResponseUtil.successResponse(result));
        }
    }

    // NOTE : 특정 게시글의 댓글 가져오기
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getComments(@PathVariable Long boardId) {
        try {
            Long userId = JwtUtil.getUserIdFromSecurityContext();  // 사용자 ID 가져오기
            BoardListResponse response = commentService.getComments(boardId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body(Map.of("message", "server error", "data", null));
        }
    }

    // NOTE : 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(HttpServletRequest request, @PathVariable Long commentId) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
    	boolean success = commentService.deleteComment(commentId, userId);
        return success ? ResponseEntity.ok("Comment deleted successfully!")
                       : ResponseEntity.status(500).body("Failed to delete comment.");
    }

    // NOTE : 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(HttpServletRequest request, @PathVariable Long commentId, @RequestBody Map<String, String> requestBody) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
        String newContent = requestBody.get("content");
        Map<String, Object> map = commentService.updateComment(commentId, newContent, userId);
        return map != null ? ResponseEntity.ok(ResponseUtil.successResponse(map))
                       : ResponseEntity.status(500).body("Failed to update comment.");
    }
}
