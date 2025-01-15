package com.board.service.admin;

import org.springframework.stereotype.Service;

import com.board.entity.admin.Notification;
import com.board.repo.admin.NotificationRepository;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // 알림 생성
    public void createNotification(String eventType, String request_module, String content, String content_detail, Long userId, Long eventId) {
        notificationRepository.save(eventType, request_module, content, content_detail, userId, eventId);
    }

    // 사용자별 알림 조회
    public List<Map<String, Object>> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

 // 사용자별 알림 조회
    public List<Map<String, Object>> getNotificationsByAllUser() {
        return notificationRepository.findByAllUser();
    }
    
    // 읽지 않은 알림 조회
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findUnreadByUserId(userId);
    }

    // 알림 읽음 처리
    public boolean markAsRead(String notificationIds, Long userId) {
    	int result =  notificationRepository.markAsRead(notificationIds);
        if(result > 0) {
            notificationRepository.save("UPDATE", "notifications", "모든 알림을 읽음 처리 했습니다.", "", userId, null);
            return true;
        }
        return false;
    }
    
    public boolean markAsAdminRead(String notificationIds, Long userId) {
    	int result =  notificationRepository.markAsAdminRead(notificationIds);
        if(result > 0) {
            notificationRepository.save("UPDATE", "notifications", "모든 알림을 읽음 처리 했습니다.", "", userId, null);
            return true;
        }
        return false;
    }
}
