package com.board.repo_jdbc;

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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // NOTE :사용자 조회
    public List<Map<String,Object>> findUserByKeyAndValue(String key, String value, Long userId) {
        String sql = "SELECT user_id, email, password, nickname, profile_url, ifnull(is_admin,'N') AS isAdmin FROM users WHERE " +
                     key + " = ? " +
                     (userId != null ? "AND user_id != ? " : "");
        
        Object[] params = userId != null ? new Object[]{value, userId} : new Object[]{value};

        List<Map<String,Object>> users = jdbcTemplate.queryForList(sql, params);
        return users;
    }
    
    // NOTE : 사용자 조회
    public Map<String,Object> findUserByuserId(Long userId) {
        String sql = "SELECT email, nickname, profile_url FROM users WHERE user_id = ? ";
        
        return jdbcTemplate.queryForMap(sql, userId);
    }

    
    // NOTE : 사용자 추가
    public Long addUser(User user) {
        String sql = "INSERT INTO users (email, password, nickname, profile_url, reg_dt) VALUES (?, ?, ?, ?, now())";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"user_id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNickname());
            ps.setString(4, user.getProfileUrl());
            return ps;
        }, keyHolder);

        Long userId = keyHolder.getKey().longValue();
        
        return userId;
    }

    // NOTE : 사용자 정보 업데이트
    public int updateUser(Long userId, User user) {
        String sql = "UPDATE users SET " +
                     "email = COALESCE(?, email), " +
                     "password = COALESCE(?, password), " +
                     "nickname = COALESCE(?, nickname), " +
                     "profile_url = COALESCE(?, profile_url) " +
                     "WHERE user_id = ?";

        String email = (user.getEmail() != null && !user.getEmail().isEmpty()) ? user.getEmail() : null;
        String password = (user.getPassword() != null && !user.getPassword().isEmpty()) ? user.getPassword() : null;
        String nickname = (user.getNickname() != null && !user.getNickname().isEmpty()) ? user.getNickname() : null;
        String profileUrl = (user.getProfileUrl() != null && !user.getProfileUrl().isEmpty()) ? user.getProfileUrl() : null;

        return jdbcTemplate.update(sql, email, password, nickname, profileUrl, userId);
    }

    // NOTE : 사용자 삭제
    public int deleteUser(Long userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        return jdbcTemplate.update(sql, userId);
    }

    // NOTE : 게시글 삭제
    public int deleteBoardsByUserId(Long userId) {
        String sql = "DELETE FROM boards WHERE user_id = ?";
        
        return jdbcTemplate.update(sql, userId);
    }

    // NOTE : 댓글 삭제
    public int deleteCommentsByUserId(Long userId) {
        String sql = "DELETE FROM comments WHERE user_id = ?";
        
        return jdbcTemplate.update(sql, userId);
    }
    
    public boolean isEmailOrNicknameDuplicate(String key, String value, Long userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE " + key + " = ? " + (userId != null ? "AND user_id != ?" : "");
        Object[] params = userId != null ? new Object[]{value, userId} : new Object[]{value};
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}