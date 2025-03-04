package com.board.repo_jdbc;

import com.board.entity.Comment;
import com.board.entity.User;
import com.board.repo_jdbc.admin.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CommentRepository {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // NOTE : 댓글 추가
    public Long addComment(Long boardId, String content, Long userId) {
        String sql = "INSERT INTO comments (board_id, content, user_id, reg_dt) VALUES (?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, boardId);
            ps.setString(2, content);
            ps.setLong(3, userId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();  // 자동 생성된 comment_id 반환
    }

    // NOTE : 특정 게시글의 댓글 가져오기
    public List<Map<String, Object>> getCommentsByBoardId(Long boardId, Long userId) {
        String sql = "SELECT " +
                     "b.board_id AS board_id, " +
                     "u.profile_url AS profileUrl, " +
                     "u.nickname, " +
                     "c.comment_id AS comment_id, " +
                     "c.content, " +
                     "c.user_id, " +
                     "c.reg_dt, " +
                     "c.chg_dt, " +
                     "CASE WHEN c.user_id = ? THEN TRUE ELSE FALSE END AS isAuthor, " +
                     "CASE WHEN c.chg_dt IS NOT NULL THEN TRUE ELSE FALSE END AS isChange " +
                     "FROM boards b " +
                     "INNER JOIN comments c ON b.board_id = c.board_id " +
                     "INNER JOIN users u ON c.user_id = u.user_id " +
                     "WHERE b.board_id = ?";
        return jdbcTemplate.queryForList(sql, userId, boardId);  // List 형태로 반환
    }

    // NOTE : 댓글 삭제
    public int deleteComment(Long commentId, Long userId) {
        String sql = "DELETE FROM comments WHERE comment_id = ? and user_id = ?";
        
        return jdbcTemplate.update(sql, commentId, userId);
    }


    // NOTE : 댓글 전체 삭제
    public int deleteAllComment(Long boardId) {
        String sql = "DELETE FROM comments WHERE board_id = ?";
        
        return jdbcTemplate.update(sql, boardId);
    }
    
    // NOTE : 댓글 수정
    public int updateComment(Long commentId, String newContent, Long userId, LocalDateTime chgDt) {
        String sql = "UPDATE comments SET content = ?, chg_dt = ? WHERE comment_id = ? and user_id = ?";
        
        Timestamp timestamp = Timestamp.valueOf(chgDt);

        return jdbcTemplate.update(sql, newContent, timestamp, commentId, userId);
    }
}