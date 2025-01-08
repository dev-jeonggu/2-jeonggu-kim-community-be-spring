package com.board.web.controller;

import com.board.dto.BoardListResponse;
import com.board.dto.BoardRequest;
import com.board.dto.BoardResponse;
import com.board.entity.Board;
import com.board.entity.Comment;
import com.board.service.BoardService;
import com.board.service.CommentService;
import com.board.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

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
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // NOTE : 댓글 추가
    @PostMapping
    public ResponseEntity<String> addComment(HttpServletRequest request, @RequestBody Map<String, Object> requestBody) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
        Long boardId = ((Number) requestBody.get("boardId")).longValue();
        String content = (String) requestBody.get("content");

        int result = commentService.addComment(boardId, content, userId);
        return result > 0 ? ResponseEntity.ok("Comment added successfully!")
                          : ResponseEntity.status(500).body("Failed to add comment.");
    }

    // NOTE : 특정 게시글의 댓글 가져오기
    @GetMapping("/{boardId}")
    public ResponseEntity<List<Comment>> getCommentsByBoardId(HttpServletRequest request, @PathVariable Long boardId) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
    	List<Comment> comments = commentService.getCommentsByBoardId(boardId, userId);
        return ResponseEntity.ok(comments);
    }

    // NOTE : 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(HttpServletRequest request, @PathVariable Long commentId) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
    	boolean success = commentService.deleteComment(commentId, userId);
        return success ? ResponseEntity.ok("Comment deleted successfully!")
                       : ResponseEntity.status(500).body("Failed to delete comment.");
    }

    // NOTE : 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(HttpServletRequest request, @PathVariable Long commentId, @RequestBody Map<String, String> requestBody) {
    	Long userId = JwtUtil.getUserIdFromSecurityContext();
        String newContent = requestBody.get("newContent");
        boolean success = commentService.updateComment(commentId, newContent, userId);
        return success ? ResponseEntity.ok("Comment updated successfully!")
                       : ResponseEntity.status(500).body("Failed to update comment.");
    }
}
