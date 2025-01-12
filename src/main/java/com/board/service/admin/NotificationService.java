package com.board.service.admin;

import org.springframework.stereotype.Service;

import com.board.entity.admin.Notification;
import com.board.repo.admin.NotificationRepository;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // 알림 생성
    public void createNotification(String eventType, String request_module, String content, Long userId) {
        notificationRepository.save(eventType, request_module, content, userId);
    }

    // 사용자별 알림 조회
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

 // 사용자별 알림 조회
    public List<Notification> getNotificationsByAllUser() {
        return notificationRepository.findByAllUser();
    }
    
    // 읽지 않은 알림 조회
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findUnreadByUserId(userId);
    }

    // 알림 읽음 처리
    public void markAsRead(Long notificationId) {
        notificationRepository.markAsRead(notificationId);
    }
}
