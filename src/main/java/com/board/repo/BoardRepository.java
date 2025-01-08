package com.board.repo;

import com.board.entity.Board;
import com.board.repo.admin.NotificationRepository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public int addBoard(String title, String content, Long userId, String imageNm, String imageUrl) {
        String sql = """
            INSERT INTO boards (title, content, user_id, image_url, image_nm, reg_dt)
            VALUES (?, ?, ?, ?, ?, NOW())
        """;

        return jdbcTemplate.update(sql, title, content, userId, imageUrl, imageNm);
    }

    public List<Map<String, Object>> getBoardList(int offset, int limit) {
        String sql = """
            SELECT b.board_id, b.title, b.reg_dt AS date, b.user_id, u.nickname, u.profile_url,
                   (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS like_cnt,
                   (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS comment_cnt,
                   (SELECT COUNT(*) FROM boardview WHERE board_id = b.board_id) AS view_cnt
            FROM boards b
            INNER JOIN users u ON b.user_id = u.user_id
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
        String sql = "DELETE FROM boards WHERE board_id = ?";
        
        
        return jdbcTemplate.update(sql, boardId);
    }

    public List<Map<String, Object>> findAllBySearch(String searchKey, String searchValue, int offset, int limit) {
    	String sql = """
    		    SELECT b.board_id, b.title, b.content, b.reg_dt AS date, b.user_id,
    		           u.nickname, u.profile_url,
    		           (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS like_cnt,
    		           (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS comment_cnt
    		    FROM boards b
    		    INNER JOIN users u ON b.user_id = u.user_id
    		    WHERE (? IS NULL OR b.title LIKE CONCAT('%', ?, '%'))
    		      AND (? IS NULL OR b.content LIKE CONCAT('%', ?, '%'))
    		    LIMIT ?, ?
    		""";

    	Object[] params = {
    		    searchKey != null ? searchKey : null,
    		    searchKey != null ? searchKey : "",
    		    searchValue != null ? searchValue : null,
    		    searchValue != null ? searchValue : "",
    		    offset,
    		    limit
    		};

        return jdbcTemplate.queryForList(sql, params);
    }
    
    public Map<String, Object> findById(Long boardId) {
        String sql = """
            SELECT b.board_id, b.title, b.content, b.reg_dt AS date, b.user_id,
                   u.nickname, u.profile_url,
                   (SELECT COUNT(*) FROM likes WHERE board_id = b.board_id) AS like_cnt,
                   (SELECT COUNT(*) FROM comments WHERE board_id = b.board_id) AS comment_cnt
            FROM boards b
            INNER JOIN users u ON b.user_id = u.user_id
            WHERE b.board_id = ?
        """;

        return jdbcTemplate.queryForMap(sql, boardId);
    }
}
