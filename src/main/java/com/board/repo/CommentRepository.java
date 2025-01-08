package com.board.repo;

import com.board.entity.Comment;
import com.board.entity.User;
import com.board.repo.admin.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepository {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // NOTE : 댓글 추가
    public int addComment(Long boardId, String content, Long userId) {
        String sql = "INSERT INTO comments (board_id, content, user_id, reg_dt) VALUES (?, ?, ?, NOW())";

        return jdbcTemplate.update(sql, boardId, content, userId);
    }

    // NOTE : 특정 게시글의 댓글 가져오기
    public List<Comment> getCommentsByBoardId(Long boardId, Long userId) {
        String sql = "SELECT " +
                     "b.board_id AS board_id, " +
                     "u.profile_url, " +
                     "u.nickname, " +
                     "c.comment_id AS comment_id, " +
                     "c.content, " +
                     "c.user_id, " +
                     "c.reg_dt, " +
                     "c.chg_dt, " +
                     "CASE WHEN c.user_id = ? THEN TRUE ELSE FALSE END AS is_author, " +
                     "CASE WHEN c.chg_dt IS NOT NULL THEN TRUE ELSE FALSE END AS is_changed " +
                     "FROM boards b " +
                     "INNER JOIN comments c ON b.board_id = c.board_id " +
                     "INNER JOIN users u ON c.user_id = u.user_id " +
                     "WHERE b.board_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId, boardId}, new CommentRowMapper());
    }

    // NOTE : 댓글 삭제
    public int deleteComment(Long commentId, Long userId) {
        String sql = "DELETE FROM comments WHERE comment_id = ? and user_id = ?";
        
        return jdbcTemplate.update(sql, commentId, userId);
    }

    // NOTE : 댓글 수정
    public int updateComment(Long commentId, String newContent, Long userId) {
        String sql = "UPDATE comments SET content = ?, chg_dt = NOW() WHERE comment_id = ? ans user_id = ?";
        
        return jdbcTemplate.update(sql, newContent, commentId, userId);
    }

    // NOTE : RowMapper
    private static class CommentRowMapper implements RowMapper<Comment> {
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment comment = new Comment();
            comment.setBoardId(rs.getLong("board_id"));
            comment.setProfileUrl(rs.getString("profile_url"));
            comment.setNickname(rs.getString("nickname"));
            comment.setCommentId(rs.getLong("comment_id"));
            comment.setContent(rs.getString("content"));
            comment.setUserId(rs.getLong("user_id"));
            comment.setRegDt(rs.getTimestamp("reg_dt") != null ? rs.getTimestamp("reg_dt").toLocalDateTime() : null);
        	comment.setChgDt(rs.getTimestamp("chg_dt") != null ? rs.getTimestamp("chg_dt").toLocalDateTime() : null);
            comment.setAuthor(rs.getBoolean("is_author"));
            comment.setChanged(rs.getBoolean("is_changed"));
            return comment;
        }
    }
}