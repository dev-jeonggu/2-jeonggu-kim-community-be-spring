package com.board.repo;

import com.board.entity.Board;
import com.board.repo.admin.NotificationRepository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class BoardRepository {
	private final JdbcTemplate jdbcTemplate;

    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getBoardById(Long boardId) {
    	String sql = """
    		    SELECT b.board_id, b.title, b.content AS boardContent, b.reg_dt AS date, b.user_id, b.image_url,
    		           u.email, u.nickname, u.profile_url,
    		           (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS like_cnt,
    		           (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS comment_cnt,
    		           (SELECT COUNT(*) FROM boardview WHERE board_id = b.board_id) AS view_cnt
    		    FROM boards b
    		    INNER JOIN users u ON b.user_id = u.user_id
    		    WHERE b.board_id = ?
    		    """;
        return jdbcTemplate.queryForMap(sql, boardId);
    }

    public Long addBoard(String title, String content, Long userId, String imageNm, String imageUrl) {
    	String sql = """
                INSERT INTO boards (title, content, user_id, image_url, image_nm, reg_dt)
                VALUES (?, ?, ?, ?, ?, NOW())
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"board_id"});
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setLong(3, userId);
            ps.setString(4, imageUrl);
            ps.setString(5, imageNm);
            return ps;
        }, keyHolder);

        Long boardId = keyHolder.getKey().longValue();
        
        return boardId;
    }

    public List<Map<String, Object>> getBoardList(int offset, int limit) {
        String sql = """
            SELECT b.board_id, b.title, b.reg_dt AS date, b.user_id, u.nickname, u.profile_url,
                   (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS like_cnt,
                   (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS comment_cnt,
                   (SELECT COUNT(*) FROM boardview WHERE board_id = b.board_id) AS view_cnt
            FROM boards b
            INNER JOIN users u ON b.user_id = u.user_id
            ORDER BY b.reg_dt desc
            LIMIT ?, ?
        """;
        return jdbcTemplate.queryForList(sql, offset, limit);
    }

    public int editBoard(Long boardId, String title, String content, String imageUrl, String imageNm, Long userId) {
        String sql = """
            UPDATE boards
            SET title = ?, content = ?, image_url = ?, image_nm = ?, chg_dt = now()
            WHERE board_id = ?
        """;
        
        return jdbcTemplate.update(sql, title, content, imageUrl, imageNm, boardId);
    }

    public int deleteBoard(Long boardId, Long userId) {
        String sql = "DELETE FROM boards WHERE board_id = ? AND user_id = ?";
        
        
        return jdbcTemplate.update(sql, boardId, userId);
    }

    public int addViewBoard(Long boardId, Long userId) {
        String sql = "INSERT INTO boardview (board_id, user_id, reg_dt) VALUES (?, ?, NOW())";

        return jdbcTemplate.update(sql, boardId, userId);
    }

    public List<Map<String, Object>> findAllBySearch(String searchKey, String searchValue, int offset, int limit) {
        // 기본적으로 조건이 없으면 모든 게시글 조회
        String whereClause = (searchKey != null && !searchKey.isEmpty() && searchValue != null && !searchValue.isEmpty())
                ? "WHERE " + searchKey + " LIKE ?"
                : "";

        String sql = """
            SELECT 
                b.board_id AS board_id,
                b.title,
                b.content,
                b.reg_dt AS date,
                b.user_id AS user_id,
                u.nickname,
                u.profile_url,
                (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS like_cnt,
                (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS comment_cnt,
                (SELECT COUNT(*) FROM boardview WHERE board_id = b.board_id) AS view_cnt
            FROM boards b
            INNER JOIN users u ON b.user_id = u.user_id
            %s
            ORDER BY b.reg_dt DESC
            LIMIT ? OFFSET ?
        """.formatted(whereClause);

        List<Object> params = new ArrayList<>();
        if (!whereClause.isEmpty()) {
            params.add("%" + searchValue + "%"); 
        }
        params.add(limit + 1); 
        params.add(offset);

        List<Map<String, Object>> boards = jdbcTemplate.queryForList(sql, params.toArray());

        return boards;
    }
    
    public Map<String, Object> findById(Long boardId, Long userId, String url) {
        String sql = """
            SELECT
                b.board_id AS board_id
            ,   b.title
            ,   b.content
            ,   b.reg_dt AS date
            ,   b.user_id AS user_id
            ,   b.image_url
            ,   b.image_nm
            ,   b.chg_dt
            ,   u.email
            ,   u.nickname
            ,   u.profile_url
            ,   ( SELECT COUNT(*) FROM likes WHERE board_id = b.board_id )as like_cnt
            ,   ( SELECT COUNT(*) FROM comments WHERE board_id = b.board_id )as comment_cnt
            ,   ( SELECT COUNT(*) FROM boardview WHERE board_id = b.board_id )as view_cnt
            ,   CASE WHEN EXISTS (
                    SELECT * 
                    FROM boards
                    WHERE user_id = ?
                    AND board_id = ?
                ) THEN TRUE 
                ELSE FALSE END AS isAuthor
            ,   CASE WHEN EXISTS (
                    SELECT * 
                    FROM boards
                    WHERE board_id = b.board_id
                    AND chg_dt is not null
                ) THEN TRUE 
                ELSE FALSE END AS isChange
            FROM boards b
            INNER JOIN users u ON b.user_id = u.user_id
            WHERE b.board_id = ?
        """;
        
        if (!url.equals("boardInfo")) {
        	sql += " AND b.user_id = ?";
            return jdbcTemplate.queryForMap(sql, userId, boardId, boardId, userId);
        }
        
        return jdbcTemplate.queryForMap(sql, userId, boardId, boardId);
    }
}
