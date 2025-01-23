package com.board.service;

import com.board.dto.BoardListResponse;
import com.board.entity.Comment;
import com.board.repo.CommentRepository;
import com.board.repo.UserRepository;
import com.board.repo.admin.NotificationRepository;
import com.board.utils.ResponseUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
	@Autowired
	private NotificationRepository notificationRepository;
	
    @Autowired
    private CommentRepository commentRepository;

    // NOTE : 댓글 추가
    public Map<String,Object> addComment(Long boardId, String content, Long userId, String nickname) {
        Long commentId = commentRepository.addComment(boardId, content, userId);
        LocalDateTime now = LocalDateTime.now();
        if(commentId > 0) {
            notificationRepository.save("INSERT", "comments", "댓글이 추가되었습니다.", content, userId, boardId);
            Map<String,Object> map = Map.of(
            		"comment_id", commentId
            		,   "board_id", boardId
            		,   "nickname", nickname
            		,   "content", content
            		,   "user_id", userId
            		,   "date", now);
            return map;
        }else {
        	return null;
        }
    }

    // NOTE : 특정 게시글의 댓글 가져오기
    public BoardListResponse getComments(Long boardId, Long userId) {
        List<Map<String, Object>> comments = commentRepository.getCommentsByBoardId(boardId, userId);
        
        // NOTE: hasNext는 페이지네이션 여부에 따라 처리할 수 있음. 여기선 예제로 false.
        boolean hasNext = false;

        return new BoardListResponse(comments, hasNext);
    }

    // NOTE : 댓글 삭제
    public boolean deleteAllComment(Long boardId, Long userId) {
        int result = commentRepository.deleteAllComment(boardId);
        if(result > 0) {
            return true;
        }
        return false;
    }

    // NOTE : 모든 댓글 삭제
    public boolean deleteComment(Long commentId, Long userId) {
        int result = commentRepository.deleteComment(commentId, userId);
        if(result > 0) {
            notificationRepository.save("DELETE", "comments", "댓글이 삭제되었습니다.", "", userId, commentId);
            return true;
        }
        return false;
    }
    
    // NOTE : 댓글 수정
    public boolean updateComment(Long commentId, String newContent, Long userId) {
        int result = commentRepository.updateComment(commentId, newContent, userId);
        if(result > 0) {
            notificationRepository.save("UPDATE", "comments", "댓글이 수정되었습니다.", newContent, userId, commentId);
            return true;
        }
        return false;
    }
}