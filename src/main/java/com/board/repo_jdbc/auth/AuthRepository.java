package com.board.repo_jdbc.auth;

import com.board.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AuthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> findByEmail(String email) {
        String sql = "SELECT user_id, nickname, password, email, profile_url, ifnull(is_admin,'N') AS isAdmin FROM users WHERE email = ?";
        return jdbcTemplate.queryForMap(sql, email);
    }
}