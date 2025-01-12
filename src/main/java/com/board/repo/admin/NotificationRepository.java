package com.board.repo.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.board.entity.admin.Notification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public NotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // NOTE : 알림 저장
    public int save(String eventType, String request_module, String content, Long userId) {
        String sql = "INSERT INTO notifications (event_type, request_module, content, user_id, is_read, reg_dt) " +
                     "VALUES (?, ?, ?, ?, 'N', now())";
        return jdbcTemplate.update(sql, eventType, request_module, content, userId);
    }

    // NOTE : 특정 사용자 알림 조회
    public List<Notification> findByUserId(Long userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ?";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), userId);
    }
    
    // NOTE : 특정 사용자 알림 조회
    public List<Notification> findByAllUser() {
        String sql = "SELECT * FROM notifications WHERE user_id";
        return jdbcTemplate.query(sql, new NotificationRowMapper());
    }
    
    // NOTE : 읽지 않은 알림 조회
    public List<Notification> findUnreadByUserId(Long userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = 0";
        return jdbcTemplate.query(sql, new NotificationRowMapper(), userId);
    }

    // NOTE : 알림 읽음 처리
    public int markAsRead(Long notificationId) {
        String sql = "UPDATE notifications SET is_read = 1, chg_dt = ? WHERE notification_id = ?";
        return jdbcTemplate.update(sql, LocalDateTime.now(), notificationId);
    }

    // NOTE : 알림 데이터 매핑
    private static class NotificationRowMapper implements RowMapper<Notification> {
        @Override
        public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
            Notification notification = new Notification();
            notification.setNotificationId(rs.getLong("notification_id"));
            notification.setEventType(rs.getString("event_type"));
            notification.setContent(rs.getString("content"));
            notification.setUserId(rs.getLong("user_id"));
            notification.setIsRead(rs.getString("is_read"));
            notification.setRegDt(rs.getTimestamp("reg_dt") != null ? rs.getTimestamp("reg_dt").toLocalDateTime() : null);
            notification.setChgDt(rs.getTimestamp("chg_dt") != null ? rs.getTimestamp("chg_dt").toLocalDateTime() : null);
            return notification;
        }
    }
}
