package com.board.repo;

import com.board.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. 사용자 조회
    public Optional<User> findUserByKeyAndValue(String key, String value, Long userId) {
        String sql = "SELECT user_id, email, password, nickname, profile_url FROM users WHERE " +
                     key + " = ? " +
                     (userId != null ? "AND user_id != ? " : "");

        Object[] params = userId != null ? new Object[]{value, userId} : new Object[]{value};

        List<User> users = jdbcTemplate.query(sql, params, new UserRowMapper());
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    // 2. 사용자 추가
    public int addUser(User user) {
        String sql = "INSERT INTO users (email, password, nickname, profile_url) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getNickname(), user.getProfileUrl());
    }

    // 3. 사용자 정보 업데이트
    public int updateUser(Long userId, User user) {
        String sql = "UPDATE users SET " +
                     "email = COALESCE(?, email), " +
                     "password = COALESCE(?, password), " +
                     "nickname = COALESCE(?, nickname), " +
                     "profile_url = COALESCE(?, profile_url) " +
                     "WHERE user_id = ?";

        return jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getNickname(), user.getProfileUrl(), userId);
    }

    // 4. 사용자 삭제
    public int deleteUser(Long userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // 5. 게시글 삭제
    public int deleteBoardsByUserId(Long userId) {
        String sql = "DELETE FROM boards WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    // 6. 댓글 삭제
    public int deleteCommentsByUserId(Long userId) {
        String sql = "DELETE FROM comments WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    
    // RowMapper 구현
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setNickname(rs.getString("nickname"));
            user.setProfileUrl(rs.getString("profile_url"));
            return user;
        }
    }

}