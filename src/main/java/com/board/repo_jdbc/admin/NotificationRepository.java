package com.board.repo_jdbc.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.board.entity.admin.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@Repository
public class NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public NotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // NOTE : 알림 저장
    public int save(String eventType, String request_module, String content, String content_detail, Long userId, Long eventId) {
        String sql = "INSERT INTO notifications (event_type, request_module, content, content_detail, user_id, event_id, is_read, reg_dt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'N', now())";
        return jdbcTemplate.update(sql, eventType, request_module, content, content_detail, userId, eventId);
    }

    // NOTE : 특정 사용자 알림 조회 (조회수 포함)
    public List<Map<String, Object>> findByUserId(Long userId) {
        String sql = """
				SELECT 
					n.notification_id 
				,	n.request_module    
				,   n.event_type 
				,	n.event_id AS boardId
				,   n.content 
				,   n.content_detail AS contentDetail
				,   u.nickname
				,   NULL AS count
				,   NULL AS notification_ids
				FROM notifications n
				INNER JOIN boards b ON n.event_id = b.board_id
				INNER JOIN users u on b.user_id = u.user_id
				WHERE IFNULL(n.is_read, 'N') = 'N'
				AND n.request_module = 'comments'
				AND b.user_id = ?
				
				UNION ALL
				
				SELECT 
					NULL AS notification_id
				,	n.request_module    
				,   n.event_type
				,	n.event_id AS boardId
				,   n.content
				,	b.title as contentDetail
				,   u.nickname
				,   COUNT(*) AS count
				,   GROUP_CONCAT(n.notification_id) AS notification_ids
				FROM notifications n
				INNER JOIN boards b ON n.event_id = b.board_id
				INNER JOIN users u on b.user_id = u.user_id
				WHERE IFNULL(n.is_read, 'N') = 'N'
				AND n.request_module = 'boardview'
				AND b.user_id = ?
				GROUP BY n.content, u.nickname, n.event_type, n.request_module, n.event_id;
        		""";
        
        return jdbcTemplate.queryForList(sql, userId, userId);
    }
    
    // NOTE : 전체 사용자 알림 조회 (조회수 제외)
    public List<Map<String, Object>> findByAllUser() {
        String sql = """
        		SELECT 
        			n.notification_id
			    ,	n.event_type
        		,	n.event_id
			    ,	n.content
			    ,	n.content_detail as contentDetail
			    ,	u.nickname
				FROM notifications n
				INNER JOIN users u ON n.user_id = u.user_id
				WHERE IFNULL(n.is_admin_read, 'N') = 'N'
				AND n.request_module not in ('boardview', 'notifications');
        		""";
        return jdbcTemplate.queryForList(sql);
    }
    
    // NOTE : 읽지 않은 알림 조회
    public List<Map<String, Object>> findUnreadByUserId(Long userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = 0";
        return jdbcTemplate.queryForList(sql, userId);
    }

    // NOTE : 알림 읽음 처리
    public int markAsRead(String notificationIds) {
        List<String> idList = Arrays.asList(notificationIds.split(","));
        String inClause = String.join(",", Collections.nCopies(idList.size(), "?"));
        String sql = "UPDATE notifications SET is_read = 'Y', chg_dt = NOW() WHERE notification_id IN (" + inClause + ")";

        return jdbcTemplate.update(sql, idList.toArray());
    }
    
    // NOTE : 알림 읽음 처리
    public int markAsAdminRead(String notificationIds) {
        List<String> idList = Arrays.asList(notificationIds.split(","));
        String inClause = String.join(",", Collections.nCopies(idList.size(), "?"));
        String sql = "UPDATE notifications SET is_admin_read = 'Y', chg_dt = NOW() WHERE notification_id IN (" + inClause + ")";

        return jdbcTemplate.update(sql, idList.toArray());
    }
}
