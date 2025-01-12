package com.board.service;

import com.board.entity.Comment;
import com.board.repo.CommentRepository;
import com.board.repo.UserRepository;
import com.board.repo.admin.NotificationRepository;

import java.util.List;
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
    public int addComment(Long boardId, String content, Long userId) {
        int result = commentRepository.addComment(boardId, content, userId);
        if(result > 0) {
            notificationRepository.save("INSERT", "comments", "댓글이 추가되었습니다 : " + content, userId);
        }
        return result;
    }

    // NOTE : 특정 게시글의 댓글 가져오기
    public List<Comment> getCommentsByBoardId(Long boardId, Long userId) {
        return commentRepository.getCommentsByBoardId(boardId, userId);
    }

    // NOTE : 댓글 삭제
    public boolean deleteComment(Long commentId, Long userId) {
        int result = commentRepository.deleteComment(commentId, userId);
        if(result > 0) {
            notificationRepository.save("UPDATE", "comments", "댓글이 삭제되었습니다", userId);
            return true;
        }
        return false;
    }

    // NOTE : 댓글 수정
    public boolean updateComment(Long commentId, String newContent, Long userId) {
        int result = commentRepository.updateComment(commentId, newContent, userId);
        if(result > 0) {
            notificationRepository.save("DELETE", "comments", "댓글이 수정되었습니다 : " +  newContent, userId);
            return true;
        }
        return false;
    }
}